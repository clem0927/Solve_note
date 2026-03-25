import {useEffect,useState,useRef} from "react";
import axios from "axios";

import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import koLocale from "@fullcalendar/core/locales/ko";

import "./styles/calendar.css";

const DEFAULT_COLOR = "#757575";

const Calendar = ({userEmail}) => {

    const calendarRef = useRef(null);

    const [categories,setCategories] = useState([]);

    const [newCategory,setNewCategory] = useState("");
    const [newColor,setNewColor] = useState("#757575");
    const [showCategoryInput,setShowCategoryInput] = useState(false);

    const [events,setEvents] = useState([]);

    const [showModal,setShowModal] = useState(false);
    const [selectedDate,setSelectedDate] = useState(null);

    const [daySchedules,setDaySchedules] = useState([]);

    const [mode,setMode] = useState("LIST");

    const requireLogin = () => {
        if(!userEmail){
            alert("로그인 후 이용 가능합니다.");
            return false;
        }
        return true;
    };

    const [form,setForm] = useState({
        id:null,
        title:"",
        description:"",
        categoryId:"",
        startAt:"",
        endAt:""
    });

    /* ======================= 카테고리 조회 ======================= */

    const fetchCategories = async()=>{

        const res = await axios.get("/back/schedule-category",{
            params:{email:userEmail}
        });

        setCategories(res.data);

    };

    /* ======================= 카테고리 생성 ======================= */

    const addCategory = async()=>{

        if(!requireLogin()) return;

        if(!newCategory.trim()) return;

        await axios.post("/back/schedule-category",{
            email:userEmail,
            name:newCategory,
            color:newColor
        });

        setNewCategory("");
        setShowCategoryInput(false);

        fetchCategories();

    };

    /* ======================= 카테고리 삭제 ======================= */

    const deleteCategory = async(id)=>{

        if(!requireLogin()) return;

        if(!window.confirm("삭제하시겠습니까?")) return;

        await axios.delete(`/back/schedule-category/${id}`);

        fetchCategories();

    };

    /* ======================= 일정 조회 ======================= */

    const fetchSchedules = async()=>{

        const res = await axios.get("/back/schedule",{
            params:{email:userEmail}
        });

        const list = res.data.map(s=>{

            const color =
                categories.find(c=>c.id === s.categoryId)?.color
                || DEFAULT_COLOR;

            return{

                id:s.id,
                title:s.title,
                start:s.startAt,
                end:s.endAt,

                backgroundColor:color,
                borderColor:color,

                extendedProps:{
                    description:s.description,
                    categoryId:s.categoryId
                }

            };

        });

        setEvents(list);

    };

    useEffect(()=>{
        if(userEmail){
            fetchCategories();
        }
    },[userEmail]);

    useEffect(()=>{
            fetchSchedules();
    },[categories]);

    /* ======================= 날짜 클릭 ======================= */

    const handleDateClick = async(info)=>{

        if(!requireLogin()) return;

        const res = await axios.get("/back/schedule",{
            params:{email:userEmail}
        });

        const list = res.data.filter(s=>{

            const start = s.startAt.slice(0,10);
            const end = s.endAt.slice(0,10);

            return start <= info.dateStr && info.dateStr <= end;

        });

        setSelectedDate(info.dateStr);
        setDaySchedules(list);

        setMode("LIST");
        setShowModal(true);

    };

    /* ======================= 이벤트 클릭 ======================= */

    const handleEventClick = (info)=>{

        if(!requireLogin()) return;

        const e = info.event;

        setForm({
            id:e.id,
            title:e.title,
            description:e.extendedProps.description || "",
            categoryId:e.extendedProps.categoryId || "",
            startAt:e.startStr,
            endAt:e.endStr
        });

        setMode("EDIT");
        setShowModal(true);

    };

    /* ======================= 일정 저장 ======================= */

    const saveSchedule = async()=>{
        if(!requireLogin()) return;

        if(form.id){

            await axios.put(`/back/schedule/${form.id}`,form);

        }else{

            await axios.post("/back/schedule",{
                ...form,
                email:userEmail
            });

        }

        await fetchSchedules();

        setShowModal(false);

    };

    /* ======================= 일정 삭제 ======================= */

    const deleteSchedule = async(id)=>{

        if(!requireLogin()) return;

        if(!window.confirm("삭제하시겠습니까?")) return;

        await axios.delete(`/back/schedule/${id}`);

        fetchSchedules();

        setShowModal(false);

    };

    /* ======================= 드래그 이동 ======================= */

    const handleEventDrop = async(info)=>{

        const event = info.event;

        await axios.put(`/back/schedule/${event.id}`,{

            startAt:event.startStr,
            endAt:event.endStr

        });

        fetchSchedules();

    };

    /* ======================= 리사이즈 ======================= */

    const handleResize = async(info)=>{

        if(!requireLogin()) return;

        const event = info.event;

        await axios.put(`/back/schedule/${event.id}`,{

            startAt:event.startStr,
            endAt:event.endStr

        });

        fetchSchedules();

    };

    /* ======================= 일정 생성 시작 ======================= */

    const startCreate = ()=>{

        if(!requireLogin()) return;

        setForm({
            id:null,
            title:"",
            description:"",
            categoryId:"",
            startAt:selectedDate+"T09:00",
            endAt:selectedDate+"T10:00"
        });

        setMode("CREATE");

    };

    /* ======================= 일정 수정 시작 ======================= */

    const startEdit = (s)=>{

        setForm({
            id:s.id,
            title:s.title,
            description:s.description || "",
            categoryId:s.categoryId || "",
            startAt:s.startAt,
            endAt:s.endAt
        });

        setMode("EDIT");

    };

    return(

        <div className="mc-layout">

            {/* ================= 사이드바 ================= */}

            <aside className="mc-sidebar">
                    <h3 className="mc-sidebar-title">일정 카테고리</h3>
                    <div className="mc-category-add">

                        {!showCategoryInput ?(

                            <button
                                className="mc-category-add-toggle"
                                onClick={()=>setShowCategoryInput(true)}
                            >
                                + 추가
                            </button>

                        ):(

                            <div className="mc-category-add-form">

                                <input
                                    className="mc-category-input"
                                    placeholder="카테고리 이름"
                                    value={newCategory}
                                    onChange={(e)=>setNewCategory(e.target.value)}
                                />

                                <input
                                    type="color"
                                    value={newColor}
                                    onChange={(e)=>setNewColor(e.target.value)}
                                />

                                <button
                                    className="mc-category-confirm"
                                    onClick={addCategory}
                                >
                                    ✔
                                </button>

                                <button
                                    className="mc-category-cancel"
                                    onClick={()=>setShowCategoryInput(false)}
                                >
                                    ✕
                                </button>

                            </div>

                        )}
                    </div>

                <ul className="mc-category-list">

                    {categories.map(cat=>(

                        <li key={cat.id} className="mc-category-item">

                            <span className="calendar-category">

                                <span
                                    className="calendar-color"
                                    style={{background:cat.color}}
                                />

                                {cat.name}

                            </span>

                            <button
                                className="mc-category-delete-btn"
                                onClick={()=>deleteCategory(cat.id)}
                            >
                                x
                            </button>

                        </li>

                    ))}

                </ul>



            </aside>

            {/* ================= 캘린더 ================= */}

            <main className="mc-main">

                <FullCalendar
                    ref={calendarRef}
                    plugins={[dayGridPlugin,interactionPlugin]}
                    initialView="dayGridMonth"
                    locale={koLocale}
                    events={events}

                    dateClick={handleDateClick}
                    eventClick={handleEventClick}

                    editable={true}
                    eventDrop={handleEventDrop}
                    eventResize={handleResize}

                    dayMaxEvents={3}
                    height="auto"

                    headerToolbar={{
                        left:"prev,next today",
                        center:"title",
                        right:""
                    }}
                />

            </main>

            {/* ================= 모달 ================= */}

            {showModal &&(

                <div className="mc-modal-overlay" onClick={()=>setShowModal(false)}>

                    <div
                        className="mc-modal-content2"
                        onClick={(e)=>e.stopPropagation()}
                    >


                        {mode === "LIST" && (

                            <>
                                <div className="calendar-day-header">
                                    <h3 className="calendar-modal-title">
                                        {selectedDate} 일정
                                    </h3>

                                    <button
                                        className="calendar-add-btn"
                                        onClick={startCreate}
                                    >
                                        + 추가
                                    </button>

                                </div>
                                <div className="calendar-day-list">

                                    {daySchedules.length === 0 && (
                                        <div className="calendar-empty">
                                            일정이 없습니다
                                        </div>
                                    )}

                                    {daySchedules.map(s=>{

                                        const start = s.startAt.slice(11,16);
                                        const end = s.endAt.slice(11,16);

                                        const color =
                                            categories.find(c=>c.id===s.categoryId)?.color
                                            || DEFAULT_COLOR;

                                        return(

                                            <div
                                                key={s.id}
                                                className="calendar-day-item"
                                            >

                                                <div className="calendar-item-left">

                                                    <span
                                                        className="calendar-dot"
                                                        style={{background:color}}
                                                    />

                                                    <div className="calendar-info">

                                                        <div className="calendar-title">
                                                            {s.title}
                                                        </div>

                                                        <div className="calender-desc">
                                                            {s.description}
                                                        </div>

                                                        <div className="calendar-time">
                                                            {start} — {end}
                                                        </div>

                                                    </div>

                                                </div>

                                                <div className="calendar-day-actions">

                                                    <button
                                                        className="calendar-icon-btn"
                                                        onClick={()=>startEdit(s)}
                                                    >
                                                        <img src="/icons/edit.svg" width="14"/>
                                                    </button>

                                                    <button
                                                        className="calendar-icon-btn delete"
                                                        onClick={()=>deleteSchedule(s.id)}
                                                    >
                                                        <img src="/icons/trash.svg" width="14"/>
                                                    </button>

                                                </div>

                                            </div>

                                        );

                                    })}

                                </div>

                            </>

                        )}

                        {(mode === "CREATE" || mode === "EDIT") && (

                            <div className="calendar-form">

                                <input
                                    className="calendar-input"
                                    placeholder="제목"
                                    value={form.title}
                                    onChange={(e)=>setForm({...form,title:e.target.value})}
                                />

                                <textarea
                                    className="calendar-textarea"
                                    placeholder="설명"
                                    value={form.description}
                                    onChange={(e)=>setForm({...form,description:e.target.value})}
                                />

                                <select
                                    className="calendar-select"
                                    value={form.categoryId}
                                    onChange={(e)=>setForm({...form,categoryId:e.target.value})}
                                >

                                    <option value="">
                                        카테고리 없음
                                    </option>

                                    {categories.map(cat=>(

                                        <option key={cat.id} value={cat.id}>
                                            {cat.name}
                                        </option>

                                    ))}

                                </select>

                                <input
                                    type="datetime-local"
                                    className="calendar-datetime"
                                    value={form.startAt}
                                    onChange={(e)=>setForm({...form,startAt:e.target.value})}
                                />

                                <input
                                    type="datetime-local"
                                    className="calendar-datetime"
                                    value={form.endAt}
                                    onChange={(e)=>setForm({...form,endAt:e.target.value})}
                                />

                                <div className="calendar-form-actions">

                                    <button
                                        className="calendar-save-btn"
                                        onClick={saveSchedule}
                                    >
                                        저장
                                    </button>

                                    <button
                                        className="calendar-cancel-btn"
                                        onClick={()=>setMode("LIST")}
                                    >
                                        취소
                                    </button>

                                </div>

                            </div>

                        )}

                    </div>

                </div>

            )}

        </div>

    );

};

export default Calendar;
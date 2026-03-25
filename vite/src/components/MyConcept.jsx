import { useState, useEffect } from "react";
import axios from "axios";
import "./styles/myconcept.css";

import ReactMarkdown from "react-markdown";
import remarkMath from "remark-math";
import rehypeKatex from "rehype-katex";
import "katex/dist/katex.min.css";

const MyConcept = ({ userEmail }) => {

    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);

    const [concepts, setConcepts] = useState([]);
    const [keyword, setKeyword] = useState("");
    const [page, setPage] = useState(0);
    const [size] = useState(12);
    const [totalPages, setTotalPages] = useState(0);

    const [newCategory, setNewCategory] = useState("");
    const [deleteCategoryName, setDeleteCategoryName] = useState("");

    const [selectedConcept, setSelectedConcept] = useState(null);

    const [editMode, setEditMode] = useState(false);
    const [editQuestion, setEditQuestion] = useState("");
    const [editAnswer, setEditAnswer] = useState("");

    // 🔴 드래그 상태
    const [dragging,setDragging] = useState(false);

    // 🔴 새 개념 모달
    const [createModal,setCreateModal] = useState(false);
    const [newQuestion,setNewQuestion] = useState("");
    const [newAnswer,setNewAnswer] = useState("");

    const [showCategoryInput,setShowCategoryInput] = useState(false);

    const requireLogin = () => {
        if(!userEmail){
            alert("로그인 후 이용 가능합니다.");
            return false;
        }
        return true;
    };

    const fetchCategories = async () => {
        try {
            const res = await axios.get("/back/category", { withCredentials: true });
            const allCategory = { id: null, name: "카테고리 없음" };
            setCategories([allCategory, ...res.data]);
        } catch (err) {
            console.error(err);
        }
    };

    const addCategory = async () => {

        if(!requireLogin()) return;

        if (!newCategory.trim()) return;

        try {

            await axios.post(
                "/back/category",
                { name: newCategory, email: userEmail },
                { withCredentials: true }
            );

            setNewCategory("");
            fetchCategories();

        } catch (err) {
            console.error(err);
        }
    };

    const deleteCategory = async (id) => {

        if(!requireLogin()) return;

        if (!window.confirm("카테고리를 삭제하시겠습니까?")) return;

        try {

            await axios.delete(`/back/category/${id}`, {
                withCredentials:true
            });

            fetchCategories();
            fetchConcepts();

        } catch (err) {
            console.error(err);
        }

    };

    const fetchConcepts = async () => {

        try {

            const params = {
                email: userEmail,
                keyword,
                page,
                size
            };

            if (selectedCategory !== null) {
                params.categoryId = selectedCategory;
            }

            const res = await axios.get("/back/concept", { params });

            const sorted = [...res.data.content].sort((a, b) => {
                return (b.isFavorite === true) - (a.isFavorite === true);
            });

            setConcepts(sorted);
            setTotalPages(res.data.totalPages);

        } catch (err) {
            console.error(err);
        }
    };

    const createConcept = async () => {
        if(!requireLogin()) return;

        if(!newQuestion.trim()) return;

        try{

            await axios.post(
                "/back/concept",
                {
                    email:userEmail,
                    question:newQuestion,
                    answer:newAnswer,
                    categoryId:selectedCategory
                },
                {withCredentials:true}
            );

            setCreateModal(false);
            setNewQuestion("");
            setNewAnswer("");

            fetchConcepts();

        }catch(err){
            console.error(err);
        }

    };

    const deleteConcept = async (id) => {
        if(!requireLogin()) return;
        if (!window.confirm("삭제하시겠습니까?")) return;

        try {

            await axios.delete(`/back/concept/${id}`, { withCredentials: true });

            setSelectedConcept(null);
            fetchConcepts();

        } catch (err) {
            console.error(err);
        }
    };

    const toggleFavorite = async (concept) => {

        try {

            await axios.put(
                `/back/concept/${concept.id}`,
                { isFavorite: !concept.isFavorite },
                { withCredentials: true }
            );

            fetchConcepts();

        } catch (err) {
            console.error(err);
        }
    };

    const updateConcept = async () => {

        try {

            await axios.put(
                `/back/concept/${selectedConcept.id}`,
                {
                    question: editQuestion,
                    answer: editAnswer
                },
                { withCredentials: true }
            );

            setEditMode(false);
            setSelectedConcept(null);
            fetchConcepts();

        } catch (err) {
            console.error(err);
        }
    };

    const handleCategorySelect = (catId) => {
        setSelectedCategory(catId);
        setPage(0);
    };

    const goPrevPage = () => {
        if (page > 0) setPage(page - 1);
    };

    const goNextPage = () => {
        if (page < totalPages - 1) setPage(page + 1);
    };

    const handleDropCategory = async (conceptId, categoryId) => {

        try {

            await axios.put(
                `/back/concept/${conceptId}/category`,
                { categoryId: categoryId ?? null },
                { withCredentials: true }
            );

            fetchConcepts();

        } catch (err) {
            console.error(err);
        }
    };

    const openConcept = (concept) => {
        setSelectedConcept(concept);
        setEditMode(false);
    };

    const startEdit = (concept) => {
        setSelectedConcept(concept);
        setEditMode(true);
        setEditQuestion(concept.question);
        setEditAnswer(concept.answer);
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    useEffect(() => {
        if (userEmail) fetchConcepts();
    }, [page, keyword, selectedCategory, userEmail]);

    return (
        <div className={`mc-layout ${dragging ? "drag-mode" : ""}`}>

            <aside className="mc-sidebar">
                <h3 className="mc-sidebar-title">개념 카테고리</h3>
                <div className="mc-category-add">

                    {!showCategoryInput ? (

                        <button
                            className="mc-category-add-toggle"
                            onClick={()=>setShowCategoryInput(true)}
                        >
                            + 추가
                        </button>

                    ) : (

                        <div className="mc-category-add-form">

                            <input
                                className="mc-category-input"
                                placeholder="카테고리 이름"
                                value={newCategory}
                                onChange={(e)=>setNewCategory(e.target.value)}
                                autoFocus
                            />

                            <button
                                className="mc-category-confirm"
                                onClick={()=>{
                                    addCategory();
                                    setShowCategoryInput(false);
                                }}
                            >
                                ✔
                            </button>

                            <button
                                className="mc-category-cancel"
                                onClick={()=>{
                                    setShowCategoryInput(false);
                                    setNewCategory("");
                                }}
                            >
                                ✕
                            </button>

                        </div>

                    )}
                    </div>

                <ul className="mc-category-list">

                    {categories.map(cat => (

                        <li
                            key={cat.id ?? "all"}
                            className={`mc-category-item ${selectedCategory === cat.id ? "selected" : ""}`}

                            onClick={() => handleCategorySelect(cat.id)}

                            onDragOver={(e) => e.preventDefault()}

                            onDragEnter={(e)=> e.currentTarget.classList.add("drag-hover")}
                            onDragLeave={(e)=> e.currentTarget.classList.remove("drag-hover")}

                            onDrop={(e) => {

                                e.currentTarget.classList.remove("drag-hover");

                                const conceptId = e.dataTransfer.getData("conceptId");

                                const categoryId = cat.id === null ? null : Number(cat.id);

                                handleDropCategory(conceptId, categoryId);

                                setDragging(false);

                            }}
                        >

                            <span className="mc-category-name">
                                {cat.name}
                            </span>

                            {cat.id !== null && (
                                <button
                                    className="mc-category-delete-btn"
                                    onClick={(e)=>{
                                        e.stopPropagation();
                                        deleteCategory(cat.id);
                                    }}
                                >
                                    x
                                </button>
                            )}

                        </li>

                    ))}

                </ul>



            </aside>

            <main className="mc-main">

                <div className="mc-search-bar">

                    <button
                        className="mc-add-btn"
                        onClick={()=>setCreateModal(true)}
                    >
                        + 개념추가
                    </button>

                    <input
                        className="mc-search-input"
                        placeholder="검색어"
                        value={keyword}
                        onChange={(e) => setKeyword(e.target.value)}
                    />

                </div>

                <div className="mc-concept-grid">

                    {!userEmail ? (

                        <div className="mc-empty">
                            로그인 후 개념을 정리할 수 있어요!
                        </div>

                    ) : concepts.length === 0 ? (

                        <div className="mc-empty">
                            아직 저장된 개념이 없어요!
                        </div>

                    ) : (


                        concepts.map(concept => (

                            <div
                                key={concept.id}
                                className="mc-concept-card"
                                draggable
                                onClick={() => openConcept(concept)}

                                onDragStart={(e)=>{
                                    e.dataTransfer.setData("conceptId", concept.id);
                                    setDragging(true);
                                }}

                                onDragEnd={()=>setDragging(false)}
                            >

                                <div className="mc-card-header">

                                    <h4 className="mc-concept-title">
                                        {concept.question}
                                    </h4>

                                    <div className="mc-card-actions">

                                    <span
                                        className={`mc-fav ${concept.isFavorite ? "active" : ""}`}
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            toggleFavorite(concept);
                                        }}
                                    >
                                        ★
                                    </span>

                                        <button
                                            className="mc-edit-btn"
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                startEdit(concept);
                                            }}
                                        >
                                            수정
                                        </button>

                                    </div>

                                </div>

                                <div className="mc-concept-preview">

                                    <ReactMarkdown
                                        remarkPlugins={[remarkMath]}
                                        rehypePlugins={[rehypeKatex]}
                                    >
                                        {(concept.answer || "").slice(0,150) + "..."}
                                    </ReactMarkdown>
                                </div>
                                <select
                                    className="mc-category-select"
                                    value={concept.category?.id ?? "default"}
                                    onClick={(e)=>e.stopPropagation()}
                                    onChange={(e)=>{

                                        const v = e.target.value;

                                        if(v === "default") return;

                                        const categoryId = v === "none" ? null : Number(v);

                                        handleDropCategory(concept.id, categoryId);

                                    }}
                                >
                                    <option value="default">카테고리 이동</option>
                                    <option value="none">카테고리 없음</option>

                                    {categories
                                        .filter(cat => cat.id !== null)
                                        .map(cat => (
                                            <option key={cat.id} value={cat.id}>
                                                {cat.name}
                                            </option>
                                        ))
                                    }
                                </select>

                                {concept.category && (
                                    <span className="mc-concept-tag">
                                {concept.category.name}
                                </span>
                                )}

                            </div>

                        ))

                    )}

                </div>

                <div className="mc-pagination">

                    <button
                        className="mc-page-btn"
                        onClick={goPrevPage}
                        disabled={page === 0}
                    >
                        이전
                    </button>

                    <span className="mc-page-info">
                        {page + 1} / {totalPages}
                    </span>

                    <button
                        className="mc-page-btn"
                        onClick={goNextPage}
                        disabled={page >= totalPages - 1}
                    >
                        다음
                    </button>

                </div>

            </main>

            {createModal && (

                <div className="mc-modal-overlay" onClick={()=>setCreateModal(false)}>

                    <div
                        className="mc-modal-content"
                        onClick={(e)=>e.stopPropagation()}
                    >

                        <h3>새 개념 추가</h3>

                        <input
                            className="mc-edit-question"
                            placeholder="질문"
                            value={newQuestion}
                            onChange={(e)=>setNewQuestion(e.target.value)}
                        />

                        <textarea
                            className="mc-edit-answer"
                            placeholder="답변"
                            value={newAnswer}
                            onChange={(e)=>setNewAnswer(e.target.value)}
                        />

                        <div className="mc-modal-actions">

                            <button
                                className="mc-save-btn"
                                onClick={createConcept}
                            >
                                생성
                            </button>

                        </div>

                    </div>

                </div>

            )}

            {selectedConcept && (

                <div
                    className="mc-modal-overlay"
                    onClick={() => setSelectedConcept(null)}
                >

                    <div
                        className="mc-modal-content"
                        onClick={(e) => e.stopPropagation()}
                    >

                        {editMode ? (
                            <>
                                <input
                                    className="mc-edit-question"
                                    value={editQuestion}
                                    onChange={(e) => setEditQuestion(e.target.value)}
                                />

                                <textarea
                                    className="mc-edit-answer"
                                    value={editAnswer}
                                    onChange={(e) => setEditAnswer(e.target.value)}
                                />

                                <div className="mc-modal-actions">

                                    <button
                                        className="mc-save-btn"
                                        onClick={updateConcept}
                                    >
                                        저장
                                    </button>

                                    <button
                                        className="mc-delete-btn"
                                        onClick={() => deleteConcept(selectedConcept.id)}
                                    >
                                        삭제
                                    </button>

                                </div>
                            </>
                        ) : (
                            <>
                                <h3>{selectedConcept.question}</h3>

                                <div className="mc-modal-body">

                                    <ReactMarkdown
                                        remarkPlugins={[remarkMath]}
                                        rehypePlugins={[rehypeKatex]}
                                    >
                                        {selectedConcept.answer}
                                    </ReactMarkdown>

                                </div>

                                <div className="mc-modal-actions">

                                    <button
                                        className="mc-delete-btn"
                                        onClick={() => deleteConcept(selectedConcept.id)}
                                    >
                                        삭제
                                    </button>

                                </div>
                            </>
                        )}

                    </div>

                </div>

            )}

        </div>
    );
};

export default MyConcept;
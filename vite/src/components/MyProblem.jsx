import { useState, useEffect } from "react";
import axios from "axios";
import "./styles/myconcept.css";

import ReactMarkdown from "react-markdown";
import remarkMath from "remark-math";
import rehypeKatex from "rehype-katex";
import "katex/dist/katex.min.css";

const MyProblem = ({ userEmail }) => {

    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);

    const [problems, setProblems] = useState([]);
    const [keyword, setKeyword] = useState("");
    const [page, setPage] = useState(0);
    const [size] = useState(12);
    const [totalPages, setTotalPages] = useState(0);

    const [newCategory, setNewCategory] = useState("");

    const [selectedProblem, setSelectedProblem] = useState(null);

    const [editMode, setEditMode] = useState(false);

    const [editQuestion, setEditQuestion] = useState("");
    const [editAnswer, setEditAnswer] = useState("");
    const [editSolution, setEditSolution] = useState("");
    const [editDifficulty, setEditDifficulty] = useState("");

    const [showAnswer, setShowAnswer] = useState(false);

    const [dragging,setDragging] = useState(false);

    // 🔴 문제 생성 모달
    const [createModal,setCreateModal] = useState(false);
    const [newQuestion,setNewQuestion] = useState("");
    const [newAnswer,setNewAnswer] = useState("");
    const [newSolution,setNewSolution] = useState("");
    const [newDifficulty,setNewDifficulty] = useState("");

    // ================= 카테고리 =================

    const fetchCategories = async () => {

        try {

            const res = await axios.get("/back/problem-category", {
                params: { email: userEmail }
            });

            const allCategory = { id: null, name: "전체" };

            setCategories([allCategory, ...res.data]);

        } catch (err) {

            console.error(err);

        }

    };

    const addCategory = async () => {

        if (!newCategory.trim()) return;

        try {

            await axios.post(
                "/back/problem-category",
                { name: newCategory, email: userEmail },
                { withCredentials: true }
            );

            setNewCategory("");

            fetchCategories();

        } catch (err) {

            console.error(err);

        }

    };

    // ================= 문제 생성 =================

    const createProblem = async () => {

        if(!newQuestion.trim()) return;

        try{

            await axios.post(
                "/back/problem",
                {
                    email:userEmail,
                    question:newQuestion,
                    answer:newAnswer,
                    solution:newSolution,
                    difficulty:newDifficulty,
                    categoryId:selectedCategory
                },
                {withCredentials:true}
            );

            setCreateModal(false);

            setNewQuestion("");
            setNewAnswer("");
            setNewSolution("");
            setNewDifficulty("");

            fetchProblems();

        }catch(err){
            console.error(err);
        }

    };

    // ================= 문제 불러오기 =================

    const fetchProblems = async () => {

        try {

            const params = {
                email: userEmail,
                keyword,
                page,
                size
            };

            if (selectedCategory) params.categoryId = selectedCategory;

            const res = await axios.get("/back/problem", { params });

            const sorted = [...res.data.content].sort(
                (a, b) => (b.isFavorite === true) - (a.isFavorite === true)
            );

            setProblems(sorted);

            setTotalPages(res.data.totalPages);

        } catch (err) {

            console.error(err);

        }

    };

    // ================= 삭제 =================

    const deleteProblem = async (id) => {

        if (!window.confirm("삭제하시겠습니까?")) return;

        try {

            await axios.delete(`/back/problem/${id}`, { withCredentials: true });

            setSelectedProblem(null);

            fetchProblems();

        } catch (err) {

            console.error(err);

        }

    };

    // ================= 즐겨찾기 =================

    const toggleFavorite = async (problem) => {

        try {

            await axios.put(
                `/back/problem/${problem.id}`,
                { isFavorite: !problem.isFavorite },
                { withCredentials: true }
            );

            fetchProblems();

        } catch (err) {

            console.error(err);

        }

    };

    // ================= 수정 =================

    const startEdit = (problem) => {

        setSelectedProblem(problem);

        setEditMode(true);

        setEditQuestion(problem.question || "");
        setEditAnswer(problem.answer || "");
        setEditSolution(problem.solution || "");
        setEditDifficulty(problem.difficulty || "");

        setShowAnswer(true);

    };

    const updateProblem = async () => {

        try {

            await axios.put(
                `/back/problem/${selectedProblem.id}`,
                {
                    question: editQuestion,
                    answer: editAnswer,
                    solution: editSolution,
                    difficulty: editDifficulty
                },
                { withCredentials: true }
            );

            setEditMode(false);

            setSelectedProblem(null);

            fetchProblems();

        } catch (err) {

            console.error(err);

        }

    };

    // ================= 카드 클릭 =================

    const openProblem = (problem) => {

        setSelectedProblem(problem);

        setEditMode(false);

        setShowAnswer(false);

    };

    // ================= 카테고리 =================

    const handleCategorySelect = (catId) => {

        setSelectedCategory(catId);

        setPage(0);

    };

    // ================= 페이징 =================

    const goPrevPage = () => {

        if (page > 0) setPage(page - 1);

    };

    const goNextPage = () => {

        if (page < totalPages - 1) setPage(page + 1);

    };

    // ================= 드래그 =================

    const handleDropCategory = async (problemId, categoryId) => {

        try {

            await axios.put(
                `/back/problem/${problemId}/category`,
                { categoryId },
                { withCredentials: true }
            );

            fetchProblems();

        } catch (err) {

            console.error(err);

        }

    };

    useEffect(() => {

        if (userEmail) fetchCategories();

    }, [userEmail]);

    useEffect(() => {

        if (userEmail) fetchProblems();

    }, [page, keyword, selectedCategory, userEmail]);

    return (

        <div className={`mc-layout ${dragging ? "drag-mode" : ""}`}>

            <aside className="mc-sidebar">

                <h3 className="mc-sidebar-title">문제 카테고리</h3>

                <ul className="mc-category-list">

                    {categories.map((cat) => (

                        <li
                            key={cat.id ?? "all"}
                            className={`mc-category-item ${selectedCategory === cat.id ? "selected" : ""}`}
                            onClick={() => handleCategorySelect(cat.id)}

                            onDragOver={(e) => e.preventDefault()}

                            onDragEnter={(e)=>e.currentTarget.classList.add("drag-hover")}
                            onDragLeave={(e)=>e.currentTarget.classList.remove("drag-hover")}

                            onDrop={(e) => {

                                e.currentTarget.classList.remove("drag-hover");

                                const problemId = e.dataTransfer.getData("problemId");

                                handleDropCategory(problemId, cat.id);

                                setDragging(false);

                            }}
                        >

                            {cat.name}

                        </li>

                    ))}

                </ul>

                <div className="mc-category-add">

                    <input
                        className="mc-category-input"
                        placeholder="새 카테고리"
                        value={newCategory}
                        onChange={(e) => setNewCategory(e.target.value)}
                    />

                    <button
                        className="mc-category-add-btn"
                        onClick={addCategory}
                    >
                        추가
                    </button>

                </div>

            </aside>

            <main className="mc-main">

                <div className="mc-search-bar">

                    <button
                        className="mc-add-btn"
                        onClick={()=>setCreateModal(true)}
                    >
                        + 문제추가
                    </button>

                    <input
                        className="mc-search-input"
                        placeholder="문제 검색"
                        value={keyword}
                        onChange={(e) => setKeyword(e.target.value)}
                    />

                </div>

                <div className="mc-concept-grid">

                    {!userEmail ? (

                        <div className="mc-empty">
                            로그인 후 문제를 확인할 수 있어요!
                        </div>

                    ) : problems.length === 0 ? (

                        <div className="mc-empty">
                            아직 저장된 문제가 없어요!
                        </div>

                    ) : (

                        problems.map((problem) => (

                            <div
                                key={problem.id}
                                className="mc-concept-card"
                                draggable
                                onClick={() => openProblem(problem)}

                                onDragStart={(e)=>{
                                    e.dataTransfer.setData("problemId", problem.id);
                                    setDragging(true);
                                }}

                                onDragEnd={()=>setDragging(false)}
                            >

                                <div className="mc-card-header">

                                    <h4 className="mc-concept-title">

                                        <ReactMarkdown
                                            remarkPlugins={[remarkMath]}
                                            rehypePlugins={[rehypeKatex]}
                                        >
                                            {problem.question}
                                        </ReactMarkdown>

                                    </h4>

                                    <div className="mc-card-actions">

                                    <span
                                        className={`mc-fav ${problem.isFavorite ? "active" : ""}`}
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            toggleFavorite(problem);
                                        }}
                                    >
                                        ★
                                    </span>

                                        <button
                                            className="mc-edit-btn"
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                startEdit(problem);
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
                                        {(problem.answer || "").slice(0,120) + "..."}
                                    </ReactMarkdown>

                                </div>

                                {problem.difficulty && (

                                    <span className="mc-concept-tag">
                                        {problem.difficulty}
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

                        <h3>새 문제 추가</h3>

                        <textarea
                            className="mc-edit-answer"
                            placeholder="문제"
                            value={newQuestion}
                            onChange={(e)=>setNewQuestion(e.target.value)}
                        />

                        <input
                            className="mc-edit-question"
                            placeholder="정답"
                            value={newAnswer}
                            onChange={(e)=>setNewAnswer(e.target.value)}
                        />

                        <textarea
                            className="mc-edit-answer"
                            placeholder="풀이"
                            value={newSolution}
                            onChange={(e)=>setNewSolution(e.target.value)}
                        />

                        <input
                            className="mc-edit-question"
                            placeholder="난이도"
                            value={newDifficulty}
                            onChange={(e)=>setNewDifficulty(e.target.value)}
                        />

                        <div className="mc-modal-actions">

                            <button
                                className="mc-save-btn"
                                onClick={createProblem}
                            >
                                생성
                            </button>

                        </div>

                    </div>

                </div>

            )}

            {selectedProblem && (

                <div
                    className="mc-modal-overlay"
                    onClick={() => setSelectedProblem(null)}
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

                                <input
                                    className="mc-edit-question"
                                    value={editAnswer}
                                    onChange={(e) => setEditAnswer(e.target.value)}
                                />

                                <textarea
                                    className="mc-edit-answer"
                                    value={editSolution}
                                    onChange={(e) => setEditSolution(e.target.value)}
                                />

                                <input
                                    className="mc-edit-question"
                                    value={editDifficulty}
                                    onChange={(e) => setEditDifficulty(e.target.value)}
                                />

                                <div className="mc-modal-actions">

                                    <button
                                        className="mc-save-btn"
                                        onClick={updateProblem}
                                    >
                                        저장
                                    </button>

                                    <button
                                        className="mc-delete-btn"
                                        onClick={() => deleteProblem(selectedProblem.id)}
                                    >
                                        삭제
                                    </button>

                                </div>
                            </>
                        ) : (

                            <>

                                <ReactMarkdown
                                    remarkPlugins={[remarkMath]}
                                    rehypePlugins={[rehypeKatex]}
                                >
                                    {selectedProblem.question}
                                </ReactMarkdown>

                                <button
                                    className="toggle-btn"
                                    onClick={() => setShowAnswer(!showAnswer)}
                                >
                                    {showAnswer ? "정답 숨기기" : "정답 보기"}
                                </button>

                                {showAnswer && (

                                    <>  <p><b>정답</b></p>

                                        <ReactMarkdown
                                            remarkPlugins={[remarkMath]}
                                            rehypePlugins={[rehypeKatex]}
                                        >
                                            {selectedProblem.answer}
                                        </ReactMarkdown>

                                        {selectedProblem.solution && (
                                            <>
                                                <b>풀이</b>

                                                <ReactMarkdown
                                                    remarkPlugins={[remarkMath]}
                                                    rehypePlugins={[rehypeKatex]}
                                                >
                                                    {selectedProblem.solution}
                                                </ReactMarkdown>
                                            </>
                                        )}
                                    </>
                                )}

                                <div className="mc-modal-actions">

                                    <button
                                        className="mc-delete-btn"
                                        onClick={() => deleteProblem(selectedProblem.id)}
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

export default MyProblem;
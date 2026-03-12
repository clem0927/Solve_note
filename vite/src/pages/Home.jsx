import { useState,useEffect,useRef } from "react";
import axios from "axios";
import "./styles/home.css";
import LoginModal from "../components/LoginModal.jsx";
import ReactMarkdown from "react-markdown";
import remarkMath from "remark-math";
import rehypeKatex from "rehype-katex";
import "katex/dist/katex.min.css";
import MyConcept from "../components/MyConcept.jsx";
import MyProblem from "../components/MyProblem.jsx";
import ProfileModal from "../components/ProfileModal.jsx";
axios.defaults.withCredentials = true;
const Home = () => {

    // ===== 모드별 채팅 =====
    const [chatMessages, setChatMessages] = useState([
        { role: "bot", text: "안녕하세요." }
    ]);

    const [conceptMessages, setConceptMessages] = useState([
        { role: "bot", text: "개념 설명 모드입니다." }
    ]);

    const [problemMessages, setProblemMessages] = useState([
        { role: "bot", text: "문제를 생성해보세요." }
    ]);

    const [input, setInput] = useState("");
    const [mode, setMode] = useState("일반채팅");

    // 개념설명 옵션
    const [difficulty, setDifficulty] = useState("쉽게");
    const [example, setExample] = useState("추가하지 않음");

    const [showLoginModal, setShowLoginModal] = useState(false);
    const [isLogin, setIsLogin] = useState(false);
    const [user, setUser] = useState(null);
    const [openProblem, setOpenProblem] = useState(null);

    const chatAreaRef = useRef(null);

    const [showProfile,setShowProfile] = useState(false);

    // 문제생성 옵션
    const [questionCount, setQuestionCount] = useState(1);

    const [loading, setLoading] = useState(false);

    // ===== 현재 모드 messages 선택 =====
    const getMessages = () => {
        if (mode === "일반채팅") return chatMessages;
        if (mode === "개념설명") return conceptMessages;
        if (mode === "문제생성") return problemMessages;
        return chatMessages;
    };

    const getSetMessages = () => {
        if (mode === "일반채팅") return setChatMessages;
        if (mode === "개념설명") return setConceptMessages;
        if (mode === "문제생성") return setProblemMessages;
        return setChatMessages;
    };

    const messages = getMessages();
    const setMessages = getSetMessages();

    const fetchUser = async () => {
        try {
            const res = await axios.get("/back/account/me", {
                withCredentials: true
            });

            setUser(res.data);
            setIsLogin(true);
            console.log(res.data);

        } catch (err) {
            setUser(null);
            setIsLogin(false);

        }
    };
    useEffect(() => {
        fetchUser();
    }, []);

    const saveConcept = async (question, answer) => {
        if (!user) {
            alert("로그인 후 저장할 수 있습니다.");
            return;
        }

        try {
            await axios.post("/back/concept", {
                question: question,
                answer: answer,
                email: user.email
            }, {
                withCredentials: true
            });

            alert("저장 완료!");
        } catch (err) {
            console.error(err);
            alert("저장 실패");
        }
    };

    const saveProblem = async (problem) => {

        if (!user) {
            alert("로그인 후 저장할 수 있습니다.");
            return;
        }

        try {

            await axios.post("/back/problem", {
                email: user.email,
                question: problem.question,
                answer: problem.answer,
                solution: problem.solution,
                difficulty: problem.difficulty
            }, {
                withCredentials: true
            });

            alert("문제 저장 완료!");

        } catch (err) {

            console.error(err);
            alert("문제 저장 실패");

        }

    };

    const checkLogin = async () => {
        try {
            const res = await axios.get("/back/account/me", {
                withCredentials: true
            });

            if (res.data) {
                setIsLogin(true);
            }
        } catch (err) {
            setIsLogin(false);
        }
    };

    useEffect(() => {
        if (chatAreaRef.current) {
            chatAreaRef.current.scrollTop = chatAreaRef.current.scrollHeight;
        }
    }, [messages]);

    useEffect(() => {
        checkLogin();
    }, [showLoginModal]);

    useEffect(() => {
        if (window.MathJax) {
            setTimeout(() => {
                window.MathJax.typesetClear();
                window.MathJax.typesetPromise();
            }, 0);
        }
    }, [messages, mode]);

    const sendMessage = async () => {

        if (!input.trim()) return;

        if (loading) return; // 연타 방지

        const userMessage = input;

        setMessages(prev => [...prev, { role: "user", text: userMessage }]);
        setInput("");

        const textarea = document.querySelector(".input-area textarea");
        if (textarea) textarea.style.height = "auto";

        setLoading(true);

        try {

            let requestData = {
                email: user?.email || "anonymous",
                message: userMessage,
                mode: "chat"
            };

            if (mode === "개념설명") {

                requestData = {
                    email: user?.email || "anonymous",
                    message: userMessage,
                    mode: "chat",
                    difficulty: difficulty,
                    example: example
                };

            }

            if (mode === "문제생성") {

                requestData = {
                    email: user?.email || "anonymous",
                    mode: "problem",
                    topic: userMessage,
                    difficulty: difficulty,
                    count: questionCount
                };

            }

            // =========================
            // 1Spring usage 체크
            // =========================

            if (user?.email) {

                await axios.post("/back/usage/chat", null, {
                    params: { email: user.email }
                });

            } else {

                await axios.post("/back/guest/chat");

            }

            // =========================
            //  Flask AI 요청
            // =========================

            const res = await axios.post("/ai/chatbot/ask", requestData);

            setMessages(prev => [
                ...prev,
                { role: "bot", text: res.data.reply }
            ]);

        } catch (err) {

            console.error(err);

            let message = "AI 응답을 가져오지 못했습니다.";

            if (err.response?.data?.message) {
                message = err.response.data.message;
            }

            setMessages(prev => [
                ...prev,
                { role: "bot", text: message }
            ]);

        } finally {

            setLoading(false);

        }

    };

    const handleKeyDown = e => {
        if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    };

    return (
        <div className="layout">

            <aside className="sidebar">
                <div className="logo-area">
                    <div className="logo">Solve Notessssssssss</div>
                    <img className="logo-img" src="images/solvenote.png" />
                </div>
                <div
                    className="sidebar-profile"
                    onClick={() => setShowProfile(true)}
                >

                    <div className="profile-avatar">
                        {user?.nickname?.charAt(0)?.toUpperCase()}
                    </div>

                    <div className="profile-info">

                        <div className="profile-name">
                            {user?.nickname || "Guest"}
                        </div>

                        <div className="profile-email">
                            {user?.email || ""}
                        </div>

                    </div>

                </div>
            </aside>

            <main className="main">

                <header className="topbar">

                    <nav className="navbar">

                        <ul className="nav-list">

                            {["일반채팅", "개념설명", "문제생성"].map(m => (
                                <li
                                    key={m}
                                    className={`nav-item ${mode === m ? "active" : ""}`}
                                    onClick={() => setMode(m)}
                                >
                                    {m}
                                </li>
                            ))}

                            <li className="nav-divider">|</li>

                            {["개념정리","문제정리"].map(m => (
                                <li
                                    key={m}
                                    className={`nav-item ${mode === m ? "active" : ""}`}
                                    onClick={() => setMode(m)}
                                >
                                    {m}
                                </li>
                            ))}


                            {!isLogin ? (
                                <li
                                    className="nav-item"
                                    onClick={() => setShowLoginModal(true)}
                                >
                                    로그인
                                </li>
                            ) : (
                                <li
                                    className="nav-item"
                                    onClick={async () => {
                                        try {
                                            await axios.post("/back/logout", {}, {
                                                withCredentials: true
                                            });
                                            setUser(null);
                                            setIsLogin(false);
                                            alert("로그아웃 되었습니다.");
                                        } catch (err) {
                                            console.error(err);
                                        }
                                    }}
                                >
                                    로그아웃
                                </li>
                            )}

                        </ul>

                    </nav>

                </header>

                <div className="chat-area" ref={chatAreaRef}>

                    {mode === "개념정리" ? (
                        <MyConcept userEmail={user?.email}/>
                    ) : mode === "문제정리" ? (
                        <MyProblem userEmail={user?.email}/>
                    ) : (
                        messages.map((msg, idx) => {

                            if (mode === "문제생성" && msg.role === "bot") {

                                let problems = [];

                                try {
                                    problems = JSON.parse(msg.text);
                                } catch {
                                    return (
                                        <div key={idx} className="message bot">
                                            {msg.text}
                                        </div>
                                    );
                                }

                                return (
                                    <div key={idx} className="problem-list">

                                        {problems.map((p, i) => {

                                            const problemKey = `${idx}-${i}`;
                                            const isOpen = openProblem === problemKey;

                                            return (
                                                <div key={i} className="problem-card">

                                                    <div className="problem-question">
                                                        <b>문제</b><br/>
                                                        <ReactMarkdown remarkPlugins={[remarkMath]} rehypePlugins={[rehypeKatex]}>
                                                            {p.question}
                                                        </ReactMarkdown>
                                                    </div>

                                                    <button
                                                        className="toggle-btn"
                                                        onClick={() => setOpenProblem(isOpen ? null : problemKey)}
                                                    >
                                                        {isOpen ? "정답/풀이 숨기기" : "정답/풀이 보기"}
                                                    </button>

                                                    {isOpen && (
                                                        <>
                                                            <div className="problem-answer">
                                                                <b>정답</b> : <ReactMarkdown remarkPlugins={[remarkMath]} rehypePlugins={[rehypeKatex]}>{p.answer}</ReactMarkdown>
                                                            </div>

                                                            <div className="problem-solution">
                                                                <b>풀이</b><br/>
                                                                <ReactMarkdown remarkPlugins={[remarkMath]} rehypePlugins={[rehypeKatex]}>
                                                                    {p.solution}
                                                                </ReactMarkdown>
                                                            </div>
                                                        </>
                                                    )}

                                                    <button
                                                        className="toggle-btn"
                                                        onClick={() => saveProblem(p)}
                                                    >
                                                        저장
                                                    </button>

                                                </div>
                                            );

                                        })}

                                    </div>
                                );

                            }

                            return (
                                <div
                                    key={idx}
                                    className={`message ${msg.role} ${mode === "개념설명" && msg.role === "bot" ? "concept-answer" : ""}`}
                                >
                                    <ReactMarkdown remarkPlugins={[remarkMath]} rehypePlugins={[rehypeKatex]}>
                                        {msg.text}
                                    </ReactMarkdown>

                                    {mode === "개념설명" && msg.role === "bot" && idx !== 0 && (
                                        <button
                                            className="toggle-btn"
                                            onClick={() => {
                                                const lastUserMessage = [...messages].reverse().find(m => m.role === "user");
                                                saveConcept(lastUserMessage?.text, msg.text);
                                            }}
                                        >
                                            저장
                                        </button>
                                    )}

                                </div>
                            );

                        })
                    )}

                </div>

                {(mode === "개념설명" || mode === "문제생성") && (
                    <div className="options-area">

                        <div className="difficulty-options">

                            <button
                                className={`difficulty-btn ${difficulty === "쉽게" ? "selected" : ""}`}
                                onClick={() => setDifficulty("쉽게")}
                            >
                                쉽게
                            </button>

                            <button
                                className={`difficulty-btn ${difficulty === "중간" ? "selected" : ""}`}
                                onClick={() => setDifficulty("중간")}
                            >
                                중간
                            </button>

                            <button
                                className={`difficulty-btn ${difficulty === "어렵게" ? "selected" : ""}`}
                                onClick={() => setDifficulty("어렵게")}
                            >
                                어렵게
                            </button>

                        </div>

                        {mode === "개념설명" && (

                            <div className="example-options">

                                <button
                                    className={`example-btn ${example === "추가하지 않음" ? "selected" : ""}`}
                                    onClick={() => setExample("추가하지 않음")}
                                >
                                    예시 없음
                                </button>

                                <button
                                    className={`example-btn ${example === "추가" ? "selected" : ""}`}
                                    onClick={() => setExample("추가")}
                                >
                                    예시 추가
                                </button>

                            </div>

                        )}

                        {mode === "문제생성" && (

                            <label>

                                문제 수

                                <div className="count-box">

                                    <button
                                        type="button"
                                        onClick={() => {
                                            if (questionCount > 1) {
                                                setQuestionCount(questionCount - 1);
                                            }
                                        }}
                                    >
                                        -
                                    </button>

                                    <span className="count-number">
                                        {questionCount}
                                    </span>

                                    <button
                                        type="button"
                                        onClick={() => {
                                            if (questionCount < 5) {
                                                setQuestionCount(questionCount + 1);
                                            }
                                        }}
                                    >
                                        +
                                    </button>

                                </div>

                            </label>
                        )}

                    </div>
                )}

                {!(mode === "개념정리" || mode === "문제정리") && (

                    <form
                        className="input-area"
                        onSubmit={(e) => {
                            e.preventDefault();
                            sendMessage();
                        }}
                    >

                        <textarea
                            placeholder="메시지를 입력하세요..."
                            value={input}
                            onChange={(e) => {
                                setInput(e.target.value);
                                e.target.style.height = "auto";
                                e.target.style.height = e.target.scrollHeight + "px";
                            }}
                            onKeyDown={handleKeyDown}
                            rows={1}
                        />

                        <button type="submit">➤</button>

                    </form>

                )}

            </main>

            <LoginModal
                show={showLoginModal}
                onClose={() => {
                    setShowLoginModal(false);
                    fetchUser();
                }}
            />
            <ProfileModal
                show={showProfile}
                onClose={()=>setShowProfile(false)}
                user={user}
                setUser={setUser}
            />
        </div>
    );

};

export default Home;
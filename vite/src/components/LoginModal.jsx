import React, { useState } from "react";
import axios from "axios";
import "./styles/loginmodal.css";

const LoginModal = ({ show, onClose }) => {
    const [tab, setTab] = useState("login"); // login | signup
    const [formData, setFormData] = useState({ email: "", password: "", confirm: "", nickname: "" });
    const [message, setMessage] = useState("");

    if (!show) return null;

    const handleChange = (e) => {
        setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");

        try {
            if (tab === "signup") {
                // 회원가입 시 체크
                if (formData.password !== formData.confirm) {
                    setMessage("비밀번호 확인이 일치하지 않습니다.");
                    return;
                }

                if (!formData.nickname.trim()) {
                    setMessage("닉네임을 입력해주세요.");
                    return;
                }

                const res = await axios.post("/back/account/signup", {
                    email: formData.email,
                    password: formData.password,
                    nickname: formData.nickname
                });

                setMessage(res.data || "회원가입 완료!");
            } else {
                const params = new URLSearchParams();
                params.append("email", formData.email);
                params.append("password", formData.password);

                await axios.post("/back/login", params, {
                    withCredentials: true,
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    }
                });

                // 로그인 후 사용자 정보 가져오기
                const me = await axios.get("/back/account/me", {
                    withCredentials: true
                });

                console.log("로그인 유저:", me.data);
                alert("로그인 성공!");
                onClose();
            }
        } catch (err) {
            console.error("Axios Error:", err);
            console.error("Response:", err.response);
            console.error("Response Data:", err.response?.data);
            console.error("Status:", err.response?.status);

            setMessage(err.response?.data?.message || "오류가 발생했습니다.");
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-container">
                <button className="close-btn2" onClick={onClose}>✕</button>

                {/* 탭 */}
                <div className="modal-tabs">
                    <div
                        className={`tab-item ${tab === "login" ? "active" : ""}`}
                        onClick={() => setTab("login")}
                    >
                        로그인
                    </div>
                    <div
                        className={`tab-item ${tab === "signup" ? "active" : ""}`}
                        onClick={() => setTab("signup")}
                    >
                        회원가입
                    </div>
                </div>

                {/* 내용 */}
                <form className="modal-form" onSubmit={handleSubmit}>
                    <label>
                        이메일
                        <input
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                    </label>

                    {tab === "signup" && (
                        <label>
                            닉네임
                            <input
                                type="text"
                                name="nickname"
                                value={formData.nickname}
                                onChange={handleChange}
                                required
                            />
                        </label>
                    )}

                    <label>
                        비밀번호
                        <input
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                        />
                    </label>

                    {tab === "signup" && (
                        <label>
                            비밀번호 확인
                            <input
                                type="password"
                                name="confirm"
                                value={formData.confirm}
                                onChange={handleChange}
                                required
                            />
                        </label>
                    )}

                    <button type="submit" className="submit-btn">
                        {tab === "login" ? "로그인" : "회원가입"}
                    </button>
                </form>

                {/* 소셜 로그인 (로그인 탭에서만 표시) */}
                {tab === "login" && (
                    <div className="social-login">

                        <div className="divider">
                            <span>또는</span>
                        </div>

                        <button
                            className="google-login-btn"
                            onClick={()=>{
                                window.location.href="/oauth2/authorization/google"
                            }}
                        >
                            <img
                                src="/images/glogo.png"
                                alt="google"
                            />
                            Google로 로그인
                        </button>

                    </div>
                )}

                {message && <div className="form-message">{message}</div>}
            </div>
        </div>
    );
};

export default LoginModal;
import { useState,useEffect } from "react";
import axios from "axios";
import "./styles/profilemodal.css";

const ProfileModal = ({ show, onClose, user, setUser }) => {

    const [nickname,setNickname] = useState(user?.nickname || "");
    const [currentPassword,setCurrentPassword] = useState("");
    const [newPassword,setNewPassword] = useState("");

    useEffect(()=>{
        setNickname(user?.nickname || "");
    },[user]);

    if(!show) return null;


    const updateNickname = async () => {

        try{

            await axios.put("/back/account/nickname",{
                nickname:nickname
            },{
                withCredentials:true
            });

            setUser({
                ...user,
                nickname:nickname
            });

            alert("닉네임 변경 완료");

        }catch(err){
            console.error(err);
            alert("닉네임 변경 실패");
        }

    };

    const changePassword = async () => {

        try{

            await axios.put("/back/account/password",{
                email:user.email,
                currentPassword:currentPassword,
                newPassword:newPassword
            },{
                withCredentials:true
            });

            alert("비밀번호 변경 완료");
            setCurrentPassword("");
            setNewPassword("");

        }catch(err){
            console.error(err);
            alert("비밀번호 변경 실패");
        }

    };

    return (

        <div className="profile-modal-overlay" onClick={onClose}>

            <div
                className="profile-modal"
                onClick={(e)=>e.stopPropagation()}
            >

                <button
                    className="profile-close"
                    onClick={onClose}
                >
                    ✕
                </button>

                <h2 className="profile-title">내 정보</h2>

                <div className="profile-avatar-large">
                    {user?.nickname?.charAt(0).toUpperCase()}
                </div>

                <div className="profile-section">

                    <label>이메일</label>
                    <input
                        value={user?.email}
                        disabled
                    />

                </div>

                <div className="profile-section">

                    <label>닉네임</label>
                    <input
                        value={nickname}
                        onChange={(e)=>setNickname(e.target.value)}
                    />

                    <button
                        className="profile-btn"
                        onClick={updateNickname}
                    >
                        닉네임 변경
                    </button>

                </div>

                <div className="profile-section">

                    <label>현재 비밀번호</label>
                    <input
                        type="password"
                        value={currentPassword}
                        onChange={(e)=>setCurrentPassword(e.target.value)}
                    />

                    <label>새 비밀번호</label>
                    <input
                        type="password"
                        value={newPassword}
                        onChange={(e)=>setNewPassword(e.target.value)}
                    />

                    <button
                        className="profile-btn"
                        onClick={changePassword}
                    >
                        비밀번호 변경
                    </button>

                </div>

            </div>

        </div>

    );

};

export default ProfileModal;
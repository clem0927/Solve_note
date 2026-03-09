import { useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const OAuthSuccess = () => {

    const navigate = useNavigate();

    useEffect(() => {

        axios.get("/back/account/me", { withCredentials: true })
            .then(res => {

                console.log("OAuth 로그인 성공:", res.data);

                // Home으로 이동
                navigate("/");

            })
            .catch(err => {
                console.log("OAuth me 요청 실패");
                console.log(err.response);
                console.log(err.response?.data);
                console.log(err.response?.status);

                console.error(err);
                navigate("/");

            });

    }, []);

    return <div>로그인 처리중...</div>;
};

export default OAuthSuccess;
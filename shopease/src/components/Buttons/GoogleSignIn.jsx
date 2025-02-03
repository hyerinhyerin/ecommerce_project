import React, { useCallback } from "react";
import { API_BASE_URL } from "../../api/constant";
import GoogleLogo from "../../assets/img/Google.png";

const GoogleSignIn = () => {
  const handleClick = useCallback(() => {
    window.location.href = API_BASE_URL + "/oauth2/authorization/google"; //서버에 요청가면 서버 의존성에 있는 oauth2-client에 의해 구글 로그인창으로 자동 연결됨.
  }, []);

  return (
    <button
      onClick={handleClick}
      className="flex justify-center items-center border w-full rounded border-gray-600 h-[48px] hover:bg-slate-50"
    >
      <img src={GoogleLogo} alt="google-icon" />
      <p className="px-2 text-gray-500">Countinue With Google</p>
    </button>
  );
};

export default GoogleSignIn;

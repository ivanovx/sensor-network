import React from "react";
import { useNavigate } from "react-router-dom";

import Storage from "../../modules/storage";

import UserService, { type UserSignIn, type UserSignUp } from "../../modules/user-service";
import AuthContext, { type Token } from "../../modules/authContext";

type Props = {
    children: React.ReactNode;
}

export default function AuthProvider({ children }: Props) {
    const navigate = useNavigate();
    const [token, setToken] = React.useState<Token>(Storage.get("token") || null);

    React.useEffect(() => {
        Storage.set("token", token);
    }, [token]);

    const signUp = (userDetails: UserSignUp) => {
        UserService
            .signUp(userDetails)
            .then(() => {
                navigate("/user/signin");
            })
            .catch((err) => {
                console.log(err);
            });
    };

    const signIn = (userDetails: UserSignIn) => {
        UserService
            .signIn(userDetails)
            .then((token: Token) => {
                setToken(token);
                navigate("/");
            })
            .catch((err) => {
                console.log(err);
            });
    };

    const signOut = () => {
        setToken(null);
        navigate("/");
    };

    return <AuthContext.Provider value={{ token, signUp, signIn, signOut }}>{children}</AuthContext.Provider>;
}
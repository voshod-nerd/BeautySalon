import React, { useContext } from 'react';
import { observer } from "mobx-react";
import AuthContext from "../contexts/AuthContext";
import Login from './Login';
import PersonalCabinet from './public/PersonalCabinet';
import Register from './Register';

const Cabinet = observer(() => {
    const authStore = useContext(AuthContext);
    const showComponent = (element) => {
        switch (element) {
            case "login": return <Login></Login>
            case 'register': return <Register></Register>
            case 'personalCabinet': return <PersonalCabinet></PersonalCabinet>
            default: return <Login></Login>;
        }
    }

    return (
        <div>
            {showComponent(authStore.getLogElement())}
        </div>);
});

export default Cabinet;
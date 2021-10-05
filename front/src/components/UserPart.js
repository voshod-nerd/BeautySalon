import React,{useContext} from 'react';
import { observer } from "mobx-react";
import AuthContext from "../contexts/AuthContext";

const UserPart = observer(() => {
    const store = useContext(AuthContext) // See the Timer definition above.
    return (
        <div>
            <p>User Part 1</p>
            <span>Seconds passed: {store.getElementCount()}</span>
        </div>);
});

export default UserPart;
import { makeAutoObservable } from "mobx";
import { login, signup } from '../utils/Api';
import { ACCESS_TOKEN, LOGIN_SUCC_TITLE, LOGIN_ERROR_401, LOGIN_SERVER_ERROR, USER_INFO, SIGN_UP_SUCCESS } from '../constants/index';


class AuthStore {
    user = {
        id: '',
        username: '',
        name: '',
        email: '',
        password: '',
        token: '',
        role: '',
        auth: false,
    }
    logElement = 'login';

    constructor() {
        makeAutoObservable(this);
        let usr = JSON.parse(localStorage.getItem(USER_INFO));
        console.log(usr);
        if (usr !== undefined && usr !== null) {
            this.user = usr;
        }
    }

    isLog() {
        return this.user.auth;
    }

    getLogElement() {
        return this.logElement;
    }

    setLogElement(value) {
        return this.logElement = value;
    }

    logOff(navigate) {
        this.user = {
            id: '',
            username: '',
            name: '',
            email: '',
            password: '',
            token: '',
            role: '',
            auth: false,
        }
        localStorage.clear();
        navigate.go(0);
    }

    setUsername(id, username, name, role, token) {
        this.user.id = id;
        this.user.username = username;
        this.user.name = name;
        this.user.token = token;
        this.user.role = role;
        this.user.auth = true;
        localStorage.setItem(USER_INFO, JSON.stringify(this.user));
    }



    get getUser() {
        return this.user;
    }

    registeUser(values) {
        const userInfo = {
            name: values.name,
            username: values.username,
            email: values.email,
            password: values.password
        }
        return signup(userInfo).then(response => Promise.resolve(SIGN_UP_SUCCESS)
        ).catch(error => Promise.reject(error));
    }


    loginUser(values) {
        const userinfo = {
            usernameOrEmail: values.username,
            password: values.password
        }
        const loginRequest = { ...userinfo };
        return login(loginRequest)
            .then(response => {
                localStorage.setItem(ACCESS_TOKEN, response.accessToken);
                console.log(JSON.stringify(response));
                this.setUsername(response.id, response.username, response.name, response.role, response.accessToken);
                return Promise.resolve(LOGIN_SUCC_TITLE);
            }).catch(error => {
                if (error.status === 401) {
                    return Promise.reject(LOGIN_ERROR_401);
                } else {
                    return Promise.reject(LOGIN_SERVER_ERROR);
                }
            });
    }
}

export default new AuthStore();
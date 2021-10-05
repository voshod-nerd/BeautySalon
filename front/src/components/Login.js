import React, { useContext } from 'react';
import { observer } from "mobx-react";
import { useHistory } from "react-router-dom";
import { Form, Input, Button, notification } from 'antd';
import AuthContext from "../contexts/AuthContext";



const layout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 16 },
};
const tailLayout = {
    wrapperCol: { offset: 4, span: 16 },
};


const openNotificationWithIcon = (type, title, message) => {
    notification[type]({
        message: title,
        description: message,
    });
};

const LoginPage = observer(() => {

    const authStore = useContext(AuthContext);
    const navigate = useHistory();

    const onFinish = (values) => {
        authStore.loginUser(values)
            .then((message) => {
                openNotificationWithIcon("success", message);
                if (authStore.getUser.role === "ROLE_ADMIN") { navigate.push("/admin"); }
                if (authStore.getUser.role === "ROLE_MASTER") { navigate.push("/master"); }
                if (authStore.getUser.role === "ROLE_USER") {
                    authStore.setLogElement('personalCabinet');
                    navigate.push("/public");
                }
            })
            .catch((messageError) => openNotificationWithIcon("error", messageError));
    }

    const onRegister = () => {
        authStore.setLogElement('register');

    }

    return (
        <div>
            <br></br>
            <br></br>
            <Form
                {...layout}
                name="basic"
                initialValues={{
                    remember: true,
                }}
                onFinish={onFinish}
            >
                <Form.Item
                    label="Имя пользвателя"
                    name="username"
                    rules={[
                        {
                            required: true,
                            message: 'Please input your username!',
                        },
                    ]}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label="Пароль"
                    name="password"
                    rules={[
                        {
                            required: true,
                            message: 'Please input your password!',
                        },
                    ]}
                >
                    <Input.Password />
                </Form.Item>
                <Form.Item {...tailLayout}>
                    <Button type="primary" htmlType="submit">
                        Войти
          </Button>
                    <Button type="link" htmlType="button" onClick={onRegister}>
                        или зарегистрируйтесь
          </Button>

                </Form.Item>
            </Form>
        </div >);
});
export default LoginPage;
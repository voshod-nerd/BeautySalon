import React, {  useContext } from 'react';
import { useHistory } from "react-router-dom";
import {
    Form,
    Input,
    Button,
    notification
} from 'antd';
import { observer } from "mobx-react";
import AuthContext from "../contexts/AuthContext";

const tailLayout = {
    wrapperCol: { offset: 4, span: 16 },
};

const openNotificationWithIcon = (type, title, message) => {
    notification[type]({
        message: title,
        description: message,
    });
};

const Register = observer(() => {
    const authStore = useContext(AuthContext);
    const navigate = useHistory();

    const onFinish = (values) => {
        authStore.registeUser(values)
            .then((message) => { openNotificationWithIcon("success", message); authStore.setLogElement('login'); navigate.push("/public"); })
            .catch((messageError) => openNotificationWithIcon("error", messageError));
    }

    return (
        <div>
            <br></br>
            <Form
                labelCol={{ span: 4 }}
                wrapperCol={{ span: 14 }}
                layout="horizontal"
                initialValues='default'
                size='medium'
                onFinish={onFinish}
            >
                <Form.Item label="ФИО пользователя" name="name">
                    <Input />
                </Form.Item>
                <Form.Item label="Логин для входа" name="username">
                    <Input />
                </Form.Item>
                <Form.Item name="email" label="Ваш адрес электронной почты">
                    <Input  type="email" />
                </Form.Item>
                <Form.Item
                    label="Пароль"
                    name="password"
                    rules={[
                        {
                            required: true,
                            message: 'Пожалуйства введите пароль',
                        },
                    ]}
                >
                    <Input.Password />
                </Form.Item>
                <Form.Item {...tailLayout}>
                    <Button type="primary" htmlType="submit">Регистрация</Button>
                </Form.Item>
            </Form>
        </div>);
});

export default Register;
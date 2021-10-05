import React, { useState, useContext, useEffect } from 'react';
import { Modal, Divider, Tag, Checkbox, Table, Button } from 'antd';
import AdminContext from '../../contexts/AdminContext';
import { observer } from "mobx-react";
import uuid from 'react-uuid';

const TypeUser = (item) => {
    switch (item.role) {
        case 'ROLE_USER': return <Tag color="blue">Клиент</Tag>
        case 'ROLE_ADMIN': return <Tag color="red">Администратор</Tag>
        case 'ROLE_MASTER': return <Tag color="green">Мастер</Tag>
        default: return <Tag color="blue">Клиент</Tag>
    }
}


const Users = observer(() => {

    const [master, setMaster] = useState({});
    const [item, setItem] = useState({
        id: "",
        role: "",
    })
    const [check, setCheck] = useState({
        user: false,
        admin: false,
        master: false,
    });
    const [isModalVisible, setIsModalVisible] = useState(false);
    const adminStore = useContext(AdminContext);
    useEffect(() => reload(), []);
    const reload = () => { adminStore.reload(); }

    const handleOk = () => {
        setIsModalVisible(false);
        adminStore.editUser(item);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };

    const changeCheck = (value) => {
        switch (value) {
            case 'ROLE_USER': { check.master = false; check.user = true; check.admin = false; item.role = "ROLE_USER"; break; }
            case 'ROLE_ADMIN': { check.master = false; check.user = false; check.admin = true; item.role = "ROLE_ADMIN"; break; }
            case 'ROLE_MASTER': { check.master = true; check.user = false; check.admin = false; item.role = "ROLE_MASTER"; break; }
            default: { check.master = false; check.user = false; check.admin = false; break; }
        }
    }

    const determineCheck = (value) => {
        if (item.role == "ROLE_USER" && value == "ROLE_USER") return true;
        if (item.role == "ROLE_ADMIN" && value == "ROLE_ADMIN") return true;
        if (item.role == "ROLE_MASTER" && value == "ROLE_MASTER") return true;
        return false;
    }


    const changeRole = (row) => {
        setItem(row);
        setIsModalVisible(true);

    }


    const columns = [
        {
            title: '#Пользователя',
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: 'ФИО',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Имя пользователя',
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: 'Тип пользователя',
            dataIndex: 'role',
            key: 'role',
            render: (cell, row, index) => TypeUser(row)
        },
        {
            title: 'Действие',
            key: 'action',
            render: (cell, row, index) => (<div><Button onClick={() => changeRole(row)} type="primary">Изменить тип</Button></div>),
        },
    ]

    return (
        <div>
            <Modal title="Смена типа пользователя" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
                <Checkbox onClick={() => changeCheck("ROLE_USER")} checked={determineCheck("ROLE_USER")}>Клиент</Checkbox>
                <Checkbox onClick={() => changeCheck("ROLE_ADMIN")} checked={determineCheck("ROLE_ADMIN")}>Администратор</Checkbox>
                <Checkbox onClick={() => changeCheck("ROLE_MASTER")} checked={determineCheck("ROLE_MASTER")}>Мастер</Checkbox>
            </Modal>
            <Table bordered={true} rowKey={(record) => record.id} columns={columns} size="small" dataSource={adminStore.getAllUsers}  ></Table>
            <Divider></Divider>
        </div>);
});

export default Users;
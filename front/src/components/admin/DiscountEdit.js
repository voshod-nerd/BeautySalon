import React, { useEffect, useContext, useState } from 'react';
import { Space, Typography, Modal, Divider, Table, Select, DatePicker, Tag, Input, Form, Button } from 'antd';
import AdminContext from '../../contexts/AdminContext';
import { observer } from "mobx-react";
import moment from 'moment';
import uuid from 'react-uuid';
const { TextArea } = Input;
const { Option } = Select;
const { Text } = Typography;

const DateTime = (begin, end) => <p> {moment(begin).format('LL')}-{moment(end).format('LL')} </p>
const discountStatus = (value) => {
    switch (value.type) {
        case "PROMOCOD": { return (<Tag color="green">Промокод</Tag>) }
        case "PERSONAL": { return <Tag color="blue">Персональная</Tag> }

        default: { return <Tag color="green">Промокод</Tag> }
    }
}

const dateFormat = 'YYYY/MM/DD';

const DiscountEdit = observer(() => {
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [addElement, setAddElement] = useState({
        startDate: "",
        endDate: "",
        name: "",
        users: null,
        type: "PROMOCOD",
        description: "",
        value: "",
    });
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);
    const adminStore = useContext(AdminContext);
    const [row, setRow] = useState([]);
    useEffect(() => reload(), []);
    const reload = () => {
        adminStore.loadDiscount();
        adminStore.loadUsers();
    }

    const [form] = Form.useForm();

    const getSelectedRow = (selectedRowKeys, selectedRows) => {
        if (selectedRows === undefined) return;
        const sRow = Object.assign({}, selectedRows[0]);
        setSelectedRowKeys(sRow);
        setRow(sRow);
        console.log(row);
        form.setFieldsValue(sRow);
    }

    function onChangeE(date, dateString) {
        row.endDate = date;
    }
    function onChangeB(date, dateString) {
        row.startDate = date;
    }

    const rowSelection = {
        onChange: getSelectedRow,
        type: 'radio'
    }

    const onFinish = (values) => {
        console.log("Finish=", values);
    };

    const showModal = () => {
        setIsModalVisible(true);
    };

    const handleOk = () => {
        setIsModalVisible(false);
        adminStore.addDiscount(addElement);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };







    const columns = [
        {
            title: '#Номер cкидки',
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: 'Название',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Дата начала : Дата конца',
            dataIndex: 'startDate',
            key: 'startDate',
            render: (cell, row, index) => <span> {DateTime(row.startDate, row.endDate)}</span>
        },
        {
            title: 'Описание',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Размер скидки',
            dataIndex: 'value',
            key: 'value',
            render: (cell, row, index) => <span> {row.value}%</span>
        },
        {
            title: 'Тип скидки',
            dataIndex: 'value',
            key: 'value',
            render: (cell, row, index) => <span> {discountStatus(row)}</span>
        },
        {
            title: 'Действие',
            key: 'action',
            render: (cell, row, index) => (
                <Space size="middle">
                    <a onClick={() => adminStore.deleteDiscount(row)} >Удалить скидку</a>
                </Space>
            ),
        },
    ]


    const onChangeInput = (name, value) => {
        switch (name) {
            case "users": { addElement.users = adminStore.getUsers.filter(x => x.id === value)[0].id; break; }
            case "end": { addElement.endDate = value; break; }
            case "start": { addElement.startDate = value; break; }
            case "name": { addElement.name = value.target.value; break; }
            case "description": { addElement.description = value.target.value; break; }
            case "discount": { addElement.value = value.target.value; break; }
            case "type": { addElement.type = value; break; }
            default: { }
        }
    }

    const modal = [
        <Modal title="Добавление скидки" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
            <Space direction="vertical">
                <Text>Тип скидки</Text>
                <Select style={{ width: 200 }} onChange={(value) => onChangeInput('type', value)} >
                    <Option value="PERSONAL">Персональная</Option>
                    <Option value="PROMOCOD">Общая (тип промокод)</Option>
                </Select>
                <Text >Название скидки</Text>
                <Input name="name" onChange={(value) => onChangeInput("name", value)}></Input>
                <Text>Начало периода скидки</Text>
                <DatePicker onChange={(value) => onChangeInput('start', value)}></DatePicker>
                <Text>Конец периода скидки</Text>
                <DatePicker onChange={(value) => onChangeInput('end', value)}></DatePicker>
                <Text>Описание скидки</Text>
                <TextArea name="description" onChange={(value) => onChangeInput("description", value)}></TextArea>
                <Text>Значение скидки</Text>
                <Input onChange={(value) => onChangeInput('discount', value)}></Input>
                <Text>Выбирете пользователя если скидка персональная</Text>
                <Select style={{ width: 200 }} onChange={(value) => onChangeInput('users', value)} >
                    {adminStore.getUsers.map(value => (
                        <Option key={uuid()} value={value.id}>{value.name}</Option>
                    ))}
                </Select>
            </Space>
        </Modal>
    ];

    return (
        <div>
            {modal}
            <Button type="primary" onClick={() => showModal()}>Добавить скидку</Button>
            <Table key={uuid()} bordered={true} rowKey={(record) => record.id} columns={columns} size="small" dataSource={adminStore.getDiscount}  ></Table>
            <Divider></Divider>
        </div>);
});

export default DiscountEdit;
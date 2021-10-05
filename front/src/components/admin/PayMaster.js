import React, { useState, useContext, useEffect } from 'react';
import { Typography, Divider, Statistic, Modal, Form, Select, Tag, Collapse, Table, Input, Button } from 'antd';
import AdminContext from '../../contexts/AdminContext';
import { observer } from "mobx-react";
import uuid from 'react-uuid';
import moment from 'moment';
import "moment/locale/ru";
const { TextArea } = Input;
const { Text } = Typography;
const { Panel } = Collapse;
const { Option } = Select;


const DateTime = (date) => <span> {moment(date).format('L')} </span>
const TimePeriod = (begin, end) => <p> {moment(begin).format('LT')}-{moment(end).format('LT')} </p>



const PayMaster = observer(() => {
    const [operation, setOperation] = useState("OUTCOME");
    const [description, setDescription] = useState("");
    const [master, setMaster] = useState(null);
    const [store, setStore] = useState([]);
    const [money, setMoney] = useState(0);
    const [row, setRow] = useState({
        master: null,
        user: null,
        dateB: new Date(),
        serviceList: [],
        totalSum: null,
        sum: null,
    });
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [cashier, setCashier] = useState({ id: "", totalSum: 0 });
    const adminStore = useContext(AdminContext);
    useEffect(() => reload(), []);
    const reload = () => {
        adminStore.loadBalance();
        adminStore.loadTransaction();
        setCashier(adminStore.getCashier);
    }

    const handleOk = () => {
        setIsModalVisible(false);
        adminStore.doneBooking(master.id);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };

    const OperationType = (value) => {
        switch (value.operation) {
            case "INCOME": { return (<Tag color="green">Приход</Tag>) }
            case "OUTCOME": { return <Tag color="red">Расход</Tag> }
            default: { }
        }
    }

    const onHandleInput = (name, value) => {
        console.log(value);
        switch (name) {
            case "OPERATION": { setOperation(value); break; }
            case "MONEY": { setMoney(value.target.value); break; }
            case "DESCRIPTION": { setDescription(value.target.value); break; }
            default: { }
        }
    }

    const payMaster = (value) => {
        setIsModalVisible(true);
        setMaster(value);
        let store = adminStore.getBookingByUserIdAndType(value.id, "PAYED");
        setStore(store);
    }


    const columnsBooking = [
        {
            title: '# Бронирования',
            dataIndex: 'id',
            key: 'id',
            render: text => <a>{text}</a>,
        },
        {
            title: 'Время записи',
            dataIndex: 'dateB',
            key: 'dateB',
            render: (cell, row, index) => TimePeriod(row.dateB, row.dateE)
        },
        { title: 'Дата заказа', dataIndex: 'dateB', key: 'dateB', render: (cell, row, index) => DateTime(row.dateB, row.dateE) },
        {
            title: 'Конечная стоимость работ',
            dataIndex: 'totalSum',
            key: 'totalSum',
            render: text => <p>{text} р</p>,
        },
    ]

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
            title: 'Действие',
            key: 'action',
            render: (cell, row, index) => (<div><Button onClick={() => payMaster(row)} type="primary">Расчитать</Button></div>),
        },
    ];

    return (
        <div>
            <Modal title="Расчет платы за выполненую работу" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
                <Form>
                    <Text level={4}>Плата мастера сотавляет 60% от итоговой цены работы</Text>
                    <Table bordered={true} rowKey={(record) => record.id} columns={columnsBooking} size="small" dataSource={store}  ></Table>
                    <Statistic title="Общая сумма выполненых работ" value={Math.round(store.reduce((a, b) => a + (b.totalSum || 0), 0))} />
                    <Statistic title="Расчитанная плата мастера" value={Math.round(store.reduce((a, b) => a + (b.totalSum || 0), 0) * 0.6)} />
                </Form>
            </Modal>
            <Table bordered={true} rowKey={(record) => record.id} columns={columns} size="small" dataSource={adminStore.getMasters}  ></Table>
            <Divider></Divider>
        </div>);
});

export default PayMaster;
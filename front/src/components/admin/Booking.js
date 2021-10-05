import React, {  useState, useContext, useEffect } from 'react';
import { Typography, List, Collapse, Divider, Tag, Table, Space, Modal, Input, DatePicker, Button } from 'antd';
import AdminContext from '../../contexts/AdminContext';
import { observer } from "mobx-react";
import uuid from 'react-uuid';
import moment from 'moment';
import "moment/locale/ru";
const { Text } = Typography;
const { Panel } = Collapse;

const DateTime = (begin, end) => <p> {moment(begin).format('LT')}-{moment(end).format('LT')} </p>
const ShowDate = (date) => <span> {moment(date).format('L')} </span>

const Booking = observer(() => {
    const [currentDate, setCurrentDate] = useState(new Date());
    const [row, setRow] = useState({
        master: null,
        user: null,
        dateB: new Date(),
        serviceList: [],
        totalSum: null,
        sum: null,
    });
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [promocod, setPromocod] = useState("");
    const adminStore = useContext(AdminContext);
    useEffect(() => reload(), []);
    const reload = () => { adminStore.loadBooking(); adminStore.loadDiscount(); }

    const closeBooking = (booking) => {
        setRow(booking);
        setIsModalVisible(true);

    }

    const handleOk = () => {
        adminStore.closeBooking(row);
        setIsModalVisible(false);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };

    const showAction = (row) => {
        console.log("Row = " + JSON.stringify(row));
        switch (row.statusBooking) {
            case "NEW": { return (<Space direction="vertical" ><a onClick={() => adminStore.apporovedBooking(row)}>Одобрить заказ</a>  <a onClick={() => adminStore.cancelBooking(row)}>Отменить заказ</a> </Space>); break; }
            case "APPROVED": { return (<Space direction="vertical"><a onClick={() => closeBooking(row)}>Закрыть заказ </a><a onClick={() => adminStore.cancelBooking(row)}>Отменить заказ</a></Space>); break; }
            default: { return (<span></span>); break; }
        }
    }

    const ShowStatus = (value) => {
        switch (value.statusBooking) {
            case "CANCELED": { return (<Tag color="red">Отмена заказа</Tag>) }
            case "PAYED": { return <Tag color="blue">Оплачено</Tag> }
            case "DONE": { return <Tag color="blue">Заказ исполнен</Tag> }
            case "APPROVED": { return <Tag color="blue">Заказ принят</Tag> }
            case "NEW": { return <Tag color="blue">Новый</Tag> }
            default: { return <Tag color="blue">Новый</Tag> }
        }
    }

    const onHandleInput = (name, value) => {
        switch (name) {
            case "currentDate": {
                setCurrentDate(value);
                adminStore.loadBookingByDate(value); break;
            }
            case "PROMOCOD": { setPromocod(value.target.value); break; }
            default: { }
        }
    }

    const ListService = (values) => <div>{values.map(x => (<Tag color="blue">{x.name}</Tag>))}</div>

    const calculateTotalSum = () => {
        let discount = adminStore.getPromokodByName(promocod);
        console.log(discount);
        let discountPrice = row.sum - row.sum * (discount / 100);
        row.totalSum = discountPrice;
    }

    const columns = [
        {
            title: '# Бронирования',
            dataIndex: 'id',
            key: 'id',
            render: text => <a>{text}</a>,
        },
        {
            title: 'Мастер',
            dataIndex: 'name',
            key: 'name',
            render: (cell, row, index) => <p> {row.master.name} </p>
        },
        {
            title: 'Дата заказа',
            dataIndex: 'dateB',
            key: 'dateB',
            render: (cell, row, index) => ShowDate(row.dateB, row.dateE)
        },
        {
            title: 'Статус заказа',
            dataIndex: 'statusBooking',
            key: 'statusBooking',
            render: (cell, row, index) => ShowStatus(row)
        },
        { title: 'Время записи', dataIndex: 'dateB', key: 'dateB', render: (cell, row, index) => DateTime(row.dateB, row.dateE) },
        {
            title: 'Действие',
            key: 'action',
            render: (cell, row, index) => (<div>{showAction(row)}</div>),
        },
    ];


    return (
        <div>
            <Modal title="Форма выполнения заказа" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
                <List>
                    <List.Item><Text level="4">Информация о записи</Text></List.Item>
                    <List.Item><Text level="4">ФИО мастера  {row.master != null ? row.master.name : ""} </Text></List.Item>
                    <List.Item><Text level="4">ФИО клиента  {row.user != null ? row.user.name : ""}</Text></List.Item>
                    <List.Item><Text level="4">Дата  заказа {ShowDate(row.dateB)} </Text></List.Item>
                    <List.Item><Text level="4">Список услуг {ListService(row.serviceList)}  </Text></List.Item>
                    <List.Item><Text level="4">Базовая цена {row.sum != null ? row.sum : ""} р. </Text></List.Item>
                    <List.Item><Text level="4">Окончательная цена {row.totalSum != null ? row.totalSum : ""} р. </Text></List.Item>
                    <List.Item><Text level="4">Промокод </Text><Input onChange={(value) => onHandleInput('PROMOCOD', value)}></Input><Button onClick={() => calculateTotalSum()} type="primary">Применить Промокод</Button></List.Item>
                </List>
            </Modal>
            <Text>Показать на дату</Text>
            <DatePicker onChange={(value) => onHandleInput('currentDate', value)} ></DatePicker>
            <br></br>
            <br></br>
            <Collapse>
                <Panel header="Записи о бронироваании" key="1">
                    <Table bordered={true} rowKey={(record) => record.id} columns={columns} size="small" dataSource={adminStore.getBooking}  ></Table>
                </Panel>
            </Collapse>

            <h4>Подробные данные</h4>
            <Divider></Divider>
        </div>);
});

export default Booking;
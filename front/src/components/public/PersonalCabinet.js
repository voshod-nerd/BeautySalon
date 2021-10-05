import React, { useContext, useEffect } from 'react';
import { Button, Form, Space, Rate, Tag, Table, Row, Col, Typography } from 'antd';
import { observer } from "mobx-react";
import moment from 'moment';
import uuid from 'react-uuid';
import { useHistory } from "react-router-dom";
import AuthContext from '../../contexts/AuthContext';
import PublicContext from '../../contexts/PublicContext';
const { Title, Text } = Typography;


const DateTime = (begin, end) => <p> {moment(begin).format('LT')}-{moment(end).format('LT')} </p>
const ListService = (values) => <div>{values.map(x => (<Tag color="blue">{x.name}</Tag>))}</div>
const ShowStatus = (value) => {
    switch (value.status) {
        case "CANCELED": { return (<Tag color="red">Отмена заказа</Tag>) }
        case "PAYED": { return <Tag color="blue">Оплачено</Tag> }
        case "DONE": { return <Tag color="blue">Заказ исполнен</Tag> }
        case "APPROVED": { return <Tag color="blue">Заказ принят</Tag> }
        case "NEW": { return <Tag color="blue">Новый</Tag> }
        default: { return <Tag color="blue">Новый</Tag> }
    }
}

const PersonalCabinet = observer(() => {
    const authStore = useContext(AuthContext);
    const publicStore = useContext(PublicContext);
    const navigator = useHistory();
    useEffect(() => reload(authStore.user.id), []);


    const reload = (userId) => {
        publicStore.loadBookingByUserId(userId);
    }

    const rateMe = (row, value) => {
        publicStore.rateBooking(row, value);
    }


    const contentRate = (row) => (
        <Rate defaultValue={row.rate} onChange={(value) => rateMe(row, value)} />
    );


    const columns = [
        {
            title: '# Бронирования',
            dataIndex: 'id',
            key: 'id',
            defaultSortOrder: 'ascend',
            sorter: (a, b) => a.id - b.id,
            render: text => <a>{text}</a>,
        },
        {
            title: 'Мастер',
            dataIndex: 'name',
            key: 'name',
            render: (cell, row, index) => <p> {row.master.name} </p>
        },
        { title: 'Время записи', dataIndex: "dateB", render: (cell, row, index) => DateTime(row.dateB, row.dateE) },
        { title: 'Список услуг', dataIndex: 'serviceList', key: 'serviceList', render: (cell, row, index) => ListService(row.serviceList) },
        { title: 'Полная цена записи', dataIndex: 'sum', key: 'sum', render: text => <p>{text} р</p>, },
        { title: 'Итоговая цена', dataIndex: 'totalSum', key: 'totalprice', render: text => <p>{text} р</p> },
        { title: 'Ваша оценка', dataIndex: 'rate', key: 'rate', render: (cell, row, index) => <p> {contentRate(row)} </p> },
        { title: 'Cтатус', dataIndex: 'status', key: 'status', render: (cell, row, index) => <ShowStatus status={row.statusBooking} /> },
        {
            title: 'Дейсвие',
            key: 'action',
            render: (cell, row, index) => (
                <Space size="middle">
                    <a onClick={() => publicStore.cancelBooking(row)} >Отменить запись</a>
                </Space>
            ),
        },
    ]

    return (
        <div key={uuid()} >
            <Title level={5}>Личные данные пользователя</Title>
            <Row>
                <Col span={24}>
                    <Form key={uuid()}
                        layout="vertical"
                    >
                        <Form.Item label="Имя пользователя" >
                            <Text code>{authStore.getUser.username}</Text>
                        </Form.Item>
                        <Form.Item label="ФИО пользователя" >
                            <Text code>{authStore.getUser.name}</Text>
                        </Form.Item>
                    </Form>
                    <Title level={5}>Список ваших записей и краткая информация</Title>
                    <Table columns={columns} rowKey={(record) => record.id} size="small" dataSource={publicStore.getUserBooking} ></Table>
                </Col>
                <Button type="primary" onClick={() => authStore.logOff(navigator)} >Выйти из учетной записи</Button>
            </Row>
        </div>);
});

export default PersonalCabinet;
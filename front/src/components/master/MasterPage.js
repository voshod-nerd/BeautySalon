
import React, { useState, useEffect, useContext } from 'react';
import { Divider, Button, Select, Table, DatePicker, Typography, Row, Col } from 'antd';
import Timeline from 'react-calendar-timeline'
// make sure you include the timeline stylesheet or the timeline will not be styled
import 'react-calendar-timeline/lib/Timeline.css'
import moment from 'moment';
import "moment/locale/ru";
import { observer } from "mobx-react";
import { useHistory } from "react-router-dom";
import AuthContext from '../../contexts/AuthContext';
import MasterContext from '../../contexts/MasterContext';
import uuid from 'react-uuid';
const { Title } = Typography;


const DateTime = (begin, end) => <p> {moment(begin).format('LT')}-{moment(end).format('LT')} </p>
const ShowDate = (date) => <span> {moment(date).format('L')} </span>
const MasterPage = observer(() => {
    const masterStore = useContext(MasterContext);
    const authStore = useContext(AuthContext);
    const navigator = useHistory();
    const reload = () => { masterStore.loadBookingByMasterAndDate(authStore.getUser, new Date()); }
    useEffect(() => reload(), []);
    const [curDate, setCurDate] = useState(new Date());
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
            render: (cell, row, index) => ShowDate(row.dateB)
        },
        { title: 'Время записи', dataIndex: 'dateB', key: 'dateB', render: (cell, row, index) => DateTime(row.dateB, row.dateE) },
    ]

    return (
        <Row>
            <Col offset={2} span={20}>
                <br></br>
                <Title level={3}>Страница мастера</Title>
                <Title level={5}>ФИО мастера: {authStore.getUser.name} </Title>
                <Title level={5}>Просмотр по дате </Title>
                <DatePicker onChange={(value, dateString) => {
                    setCurDate(value);
                    masterStore.loadBookingByMasterAndDate(authStore.getUser, value);
                }}></DatePicker>
                <Divider></Divider>
                <Row>
                    <Col span={18}>
                        <Timeline
                            key={uuid()}
                            canMove={false}
                            canResize={false}
                            groups={masterStore.sheduleItems != undefined ? masterStore.sheduleItems.masters : []}
                            items={masterStore.sheduleItems != undefined ? masterStore.sheduleItems.items : []}
                            defaultTimeStart={moment(curDate).startOf('day').add(9, 'hour')}
                            defaultTimeEnd={moment(curDate).startOf('day').add(19, 'hour')}
                            visibleTimeStart={moment(curDate).startOf('day').add(9, 'hour').millisecond()}
                            visibleTimeEnd={moment(curDate).startOf('day').add(19, 'hour').millisecond()}
                        >
                        </Timeline>
                    </Col>
                </Row>
                <Divider></Divider>
                <Title level={4}>Список всех бронировании за {moment(curDate).format('LL')} </Title>
                <Table columns={columns} size="small" dataSource={masterStore.getBooking(authStore.getUser) != undefined ? masterStore.getBooking(authStore.getUser) : []} ></Table>
                <Button type="primary" onClick={() => authStore.logOff(navigator)} >Выйти из учетной записи</Button>
                <Divider></Divider>
            </Col>
        </Row>);
}
);

export default MasterPage;
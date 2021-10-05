import React, { useState, useEffect, useContext } from 'react';
import { Collapse, Divider, TimePicker, Button, Row, Col, Select, Table, DatePicker, Typography, Form, notification } from 'antd';
import Timeline from 'react-calendar-timeline'

// make sure you include the timeline stylesheet or the timeline will not be styled
import 'react-calendar-timeline/lib/Timeline.css'
import moment from 'moment';
import "moment/locale/ru";
import { observer } from "mobx-react";
import PublicContext from "../../contexts/PublicContext";
import AuthContext from '../../contexts/AuthContext';
import uuid from 'react-uuid';
const { Title } = Typography;
const { Panel } = Collapse;


const ListServices = (props) => {
    const list = props.services;
    const handleChange = selectedItems => {
        console.log(selectedItems);
        props.setChoosedServices(list.filter(x => selectedItems.includes(x.id)));
    }
    return (<div>
        <Select
            mode="multiple"
            onChange={handleChange}
            style={{ width: '100%' }}
            value={props.choosedServices.length > 0 ? props.choosedServices.map(x => x.id) : []}
        >
            {list.map(item => (
                <Select.Option key={uuid()} value={item.id}>
                    {item.name} Цена услуги: {item.price} Длитетельность выполнения {item.durationInMinute}
                </Select.Option>
            ))}
        </Select>
    </div>
    );
}

const ListMasters = (props) => {
    const list = props.masters;
    const handleChange = selectedItem => {
        console.log(selectedItem);
        props.setChoosedMaster(list.filter(x => selectedItem === x.id)[0]);

    }

    return (<div>
        <Select onChange={handleChange} style={{ width: '100%' }}>
            {list.map(item => (
                <Select.Option key={uuid()} value={item.id}>
                    {item.name}
                </Select.Option>
            ))}
        </Select>
    </div>
    );
}

const DateTime = (begin, end) => <p> {moment(begin).format('LT')}-{moment(end).format('LT')} </p>
const Orders = observer(() => {
    const reload = () => { publicStore.reloadData(new Date()); }
    const publicStore = useContext(PublicContext);
    const authStore = useContext(AuthContext);
    useEffect(() => reload(), []);
    const [choosedMaster, setChoosedMaster] = useState(null);
    const [choosedServices, setChoosedServices] = useState([]);
    const [timeLine, setTimeLine] = useState({
        masters: [],
        items: [],
    });
    const [choosedDate, setChoosedDate] = useState(new Date());
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
        { title: 'Время записи', dataIndex: 'dateB', key: 'dateB', render: (cell, row, index) => DateTime(row.dateB, row.dateE) },
    ]


    const format = 'HH:mm';
    const layout = {
        labelCol: { span: 16 },
        wrapperCol: { span: 16 },
    };

    const onChangeDateTime = (value, dateString) => {
        let time = moment(value);
        let date = moment(curDate).startOf('day').add("hour", time.hours()).add("minute", time.minutes());
        setChoosedDate(date);

    }

    const openNotificationWithIcon = (type, title, message) => {
        notification[type]({
            message: title,
            description: message,
        });
    };

    const handleOk = () => {
        publicStore.createBooking(choosedServices, choosedMaster, authStore.getUser, choosedDate)
            .then((response) => {
                if (response.success) {
                    openNotificationWithIcon("success", response.message);
                    setChoosedMaster([]);
                    setChoosedServices([]);
                    publicStore.loadBookingByDate(curDate).then(response => { setTimeLine(response) });
                }
                else openNotificationWithIcon("error", response.message)
            })
            .catch((messageError) => openNotificationWithIcon("error", messageError));
    };


    const returnSkills = (all, master) => {
        try {
            return all.filter(x => master.skills.map(y => y.id).includes(x.id));
        } catch (e) { return []; }
    }

    return (
        <div>
            <br></br>
            <Title level={4}>Просмотр по дате </Title>
            <DatePicker onChange={(value, dateString) => {
                setCurDate(value);
                publicStore.loadBookingByDate(value).then(response => { console.log(JSON.stringify(response)); setTimeLine(response) });
            }}></DatePicker>
            <Divider></Divider>
            <Row>
                <Col span={18}>
                    <Timeline
                        key={uuid()}
                        canMove={false}
                        canResize={false}
                        groups={timeLine != undefined ? timeLine.masters : []}
                        items={timeLine != undefined ? timeLine.items : []}
                        defaultTimeStart={moment(curDate).startOf('day').add(9, 'hour')}
                        defaultTimeEnd={moment(curDate).startOf('day').add(19, 'hour')}
                        visibleTimeStart={moment(curDate).startOf('day').add(9, 'hour').millisecond()}
                        visibleTimeEnd={moment(curDate).startOf('day').add(19, 'hour').millisecond()}

                    />
                </Col>
            </Row>
            <Divider></Divider>
            <Collapse defaultActiveKey={['1']} >
                <Panel header="Сделать запись" key="1">
                    <Form layout="vertical" size="small" {...layout}>
                        <Form.Item label="Выберете время записи">
                            <TimePicker defaultValue={moment(curDate)} onChange={onChangeDateTime} disabledHours={() => [0, 1, 2, 3, 4, 5, 6, 7, 19, 18, 19, 20, 21, 22, 23, 24]} minuteStep={15} format={format} />
                        </Form.Item>
                        <Form.Item label="Выбрать мастера">
                            <ListMasters masters={publicStore.masterList} setChoosedMaster={setChoosedMaster} ></ListMasters>
                        </Form.Item>
                        <Form.Item label="Список услуг для бронирования">
                            <ListServices choosedServices={choosedServices} services={returnSkills(publicStore.serviceList, choosedMaster)} setChoosedServices={setChoosedServices}></ListServices>
                        </Form.Item>
                        <Button type="primary" onClick={() => handleOk()}>Сделать запись</Button>
                    </Form>
                </Panel>
            </Collapse>
            <Title level={4}>Список всех бронировании за {moment(curDate).format('LL')} </Title>
            <Table columns={columns} size="small" dataSource={publicStore.getCurrentBooking} ></Table>
        </div>);
}
);

export default Orders;
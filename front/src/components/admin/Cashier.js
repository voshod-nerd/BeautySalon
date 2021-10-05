import React, { useState, useContext, useEffect } from 'react';
import { Divider, Modal, Form, Select, Statistic, Row, Col, Tag, Collapse, Table, Input, Button } from 'antd';
import AdminContext from '../../contexts/AdminContext';
import { observer } from "mobx-react";
import uuid from 'react-uuid';
import moment from 'moment';
import "moment/locale/ru";
const { TextArea } = Input;
const { Panel } = Collapse;
const { Option } = Select;

const DateTime = (date) => <span> {moment(date).format('L')} </span>
const Cashier = observer(() => {
    const [operation, setOperation] = useState("OUTCOME");
    const [description, setDescription] = useState("");
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
        adminStore.reload();
        adminStore.loadBalance().then(response => setCashier(response));
    }

    const handleOk = () => {

        let transaction = {
            operation: operation,
            sum: money,
            description: description
        }
        adminStore.makeTransaction(transaction).then(response =>
            adminStore.loadBalance().then(response => setCashier(response)));
        console.log(cashier);
        setIsModalVisible(false);
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

    const columns = [
        {
            title: '# Операции',
            dataIndex: 'id',
            key: 'id',
            render: text => <a>{text}</a>,
        },
        {
            title: 'Дата операции',
            dataIndex: 'time',
            key: 'time',
            render: (cell, row, index) => DateTime(row.time)
        },
        {
            title: 'Тип операции',
            dataIndex: 'operation',
            key: 'operation',
            render: (cell, row, index) => OperationType(row)
        },
        { title: 'Сумма операции', dataIndex: 'sum', key: 'sum' },
        {
            title: 'Краткое описание',
            dataIndex: 'description',
            key: 'description',
            render: text => <p>{text}</p>,
        },
    ];

    return (
        <div>
            <Modal title="Операция с кассой" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
                <Form>
                    <Form.Item label="Тип операции">
                        <Select defaultValue="OUTCOME" style={{ width: 220 }} onChange={(value) => onHandleInput("OPERATION", value)}>
                            <Option value="INCOME">Внесение наличности</Option>
                            <Option value="OUTCOME">Снятие наличности</Option>
                        </Select>
                    </Form.Item>
                    <Form.Item label="Cумма снятия,вноса ">
                        <Input onChange={(value) => onHandleInput("MONEY", value)}></Input>
                    </Form.Item>
                    <Form.Item label="Описание">
                        <TextArea onChange={(value) => onHandleInput("DESCRIPTION", value)}></TextArea>
                    </Form.Item>
                </Form>
            </Modal>
            <Row gutter={16}>
                <Col span={12}>
                    <Statistic title="Баланс кассы (рубли)" value={cashier != undefined ? cashier.totalSum : 0} />
                </Col>
                <Col span={4}>
                    <Button style={{ marginTop: 16 }} onClick={() => setIsModalVisible(true)} type="primary">Кассовая операция</Button>
                </Col>
            </Row>
            <Collapse defaultActiveKey={['1']} >
                <Panel header="Список операций по кассе" key="1">
                    <Table bordered={true} rowKey={(record) => record.id} columns={columns} size="small" dataSource={adminStore.getTransaction}  ></Table>
                </Panel>
            </Collapse>
            <Divider></Divider>
        </div>);
});

export default Cashier;
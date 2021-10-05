import React, { useEffect, useContext, useState } from 'react';
import { Select, Modal, Collapse, Divider, Table, Checkbox, Tag, Input, Form, Button, Col, Row } from 'antd';
import AdminContext from '../../contexts/AdminContext';
import { observer } from "mobx-react";
import uuid from 'react-uuid';
const { TextArea } = Input;
const { Panel } = Collapse;
const { Option } = Select;

const isActiveService = (active) => { return active ? <Tag color="green">Да</Tag> : <Tag color="red">Нет</Tag> }

const ServiceEdits = observer(() => {
    const [row, setRow] = useState(null);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [consumList, setConsimList] = useState([]);
    const [element, setElement] = useState({
        idMaterial: null,
        serviceId: null,
        name: "",
        quantity: 0,
    });
    const adminStore = useContext(AdminContext);
    useEffect(() => reload(), []);
    const reload = () => { adminStore.loadServices(); }

    const [form] = Form.useForm();

    const rowSelection = {
        onChange: (selectedRowKeys, selectedRows) => {
            setRow(selectedRows[0]);
            form.setFieldsValue(selectedRows[0]);
            setConsimList(adminStore.getMaterialByServiceId(selectedRows[0].id));
            element.serviceId = selectedRows[0].id;
            console.log(JSON.stringify(consumList));
        },
        type: 'radio'
    }

    const onFinish = (values) => {
        // values.skills=master.skills;
        console.log("Finish service=", JSON.stringify(values));
    };


    const showModal = () => {
        setIsModalVisible(true);
    };

    const handleOk = () => {
        setIsModalVisible(false);
        adminStore.addConsumeMaterial(element);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };

    const columnMaterial = [
        {
            title: '#Номер',
            dataIndex: 'idMaterial',
            key: 'idMaterial'
        },
        {
            title: 'Название материала',
            dataIndex: 'name',
            key: 'name',
            render: (cell, row, index) => <span>{row.name}</span>
        },
        {
            title: 'Количество материала',
            dataIndex: 'quantity',
            key: 'quantity',
        },
        {
            title: 'Действие',
            dataIndex: 'action',
            key: 'action',
            render: (cell, row, index) => <span> <a onClick={() => adminStore.deleteConsume(row)} >Удалить из списка</a></span>
        }
    ];


    const columns = [
        {
            title: '#Номер услуги',
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: 'Название',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Цена услуги (рубли)',
            dataIndex: 'price',
            key: 'price',
        },
        {
            title: 'Длительность в минутах',
            dataIndex: 'durationInMinute',
            key: 'durationInMinute',
        },
        {
            title: 'Активна ли услуга',
            dataIndex: 'isActive',
            key: 'isActive',
            render: (cell, row, index) => <span> {isActiveService(row.active)} </span>
        }
    ]

    const deleteMaterial = (value) => {
        setIsModalVisible(true);
    }

    const onChangeInput = (name, value) => {
        switch (name) {
            case "serviceId": {
                let el = adminStore.getMaterials.filter(x => x.id === value)[0];
                element.idMaterial = el.id;
                element.name = el.name;
                break;
            }
            case "quantity": { element.quantity = value.target.value; break; }
            default: { }
        }
    }

    return (
        <div>
            <Modal title="Добавить материал для услуги" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
                <Form>
                    <Form.Item label="Тип потребляемного материала">
                        <Select onChange={(value) => onChangeInput("serviceId", value)}>
                            {adminStore.getMaterials.map((value) => <Option key={uuid()} value={value.id}>{value.name}</Option>)}
                        </Select>
                    </Form.Item>
                    <Form.Item label="Количество потребляемого материала">
                        <Input onChange={(value) => onChangeInput("quantity", value)}></Input>
                    </Form.Item>
                </Form>
            </Modal>
            <Table bordered={true} rowKey="id" rowSelection={rowSelection} columns={columns} size="small" dataSource={adminStore.getServices}  ></Table>
            <Collapse defaultActiveKey={['1']} >
                <Panel header="Подробное описание" key="1">
                    <Row>
                        <Col span={12}>
                            <Form form={form} onFinish={onFinish} key={uuid()} formLayout='vertical' >
                                <Form.Item style={{ display: 'none' }} name="id">
                                    <Input />
                                </Form.Item>
                                <Form.Item label="Название услуги" name="name">
                                    <Input />
                                </Form.Item>
                                <Form.Item label="Имя пользователя" name="username" >
                                    <Input />
                                </Form.Item>
                                <Form.Item label="Цена" name="price" >
                                    <Input />
                                </Form.Item>
                                <Form.Item label="Длительность в минутах" name="durationInMinute" >
                                    <Input />
                                </Form.Item>
                                <Form.Item valuePropName="checked" name="active" >
                                    <Checkbox> Активна ли услуга</Checkbox>
                                </Form.Item>
                                <Form.Item label="Описание услуги" name="description">
                                    <TextArea rows={4} />
                                </Form.Item>
                                <Form.Item >
                                    <Button type="primary" htmlType="submit">Сохранить</Button>
                                </Form.Item>
                            </Form>
                        </Col>
                    </Row>
                </Panel>
                <Panel header="Список потребляемых материалов для услуги" key="2">
                    <Button onClick={() => deleteMaterial()} type="primary">Добавить материал в список</Button>
                    <Table bordered={true} rowKey="id" columns={columnMaterial} size="small" dataSource={adminStore.getConsumeList}  ></Table>
                </Panel>
            </Collapse>
            <Divider></Divider>
        </div>);
});

export default ServiceEdits;
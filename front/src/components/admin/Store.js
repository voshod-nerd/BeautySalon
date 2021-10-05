import React, { useEffect, useContext, useState } from 'react';
import { Select, Modal, Divider, Table, Space, Tag, Input, Form, Button, Col, Row, notification } from 'antd';
import AdminContext from '../../contexts/AdminContext';
import { observer } from "mobx-react";
import uuid from 'react-uuid';
const { Option } = Select;


const isActiveService = (active) => { return active ? <Tag color="green">Да</Tag> : <Tag color="red">Нет</Tag> }

const Store = observer(() => {
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [val, setVal] = useState({ idMaterial: "", value: 0 });
    const [element, setElement] = useState({

    });
    const [type, setType] = useState("MINUS");
    const adminStore = useContext(AdminContext);
    useEffect(() => reload(), []);
    const waring = () => {
        console.log("Waring function");
        let warningList = adminStore.getMaterials.filter(x => x.value < 100);
        if (warningList.length > 0) {
            let warningText = "";
            warningList.forEach(x => warningText = warningText + " Материал : " + x.name + " : Количество " + x.value + " \n")
            notification.error({
                message: 'Количество материала на складе катострафически малое',
                description:
                    warningText
            });
        }
    }
    const reload = () => { adminStore.loadMaterial(); waring(); }


    const handleOk = () => {
        setIsModalVisible(false);
        if (type == "PLUS") { adminStore.plusMaterialCount(val);  }
        if (type == "MINUS") { adminStore.minusMaterialCount(val); }
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };

    const operation = (type, row) => {
        setIsModalVisible(true);
        setType(type);
        val.idMaterial = row.id;
    }

    const onHandleInput = (name, value) => {
        console.log(value);
        switch (name) {
            case "VALUE": { val.value = value.target.value; break; }
            case "SELECT_MEASURE": { element.unitMeasure = value; break; }
            case "MATERIAL_NAME": { element.name = value.target.value; break; }
            case "MATERIAL_COUNT": { element.value = value.target.value; break; }
            default: { }
        }
    }

    const addMaterial = () => {
        adminStore.addNewMaterial(element);
    }

    const columns = [
        {
            title: 'Название материала',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'еденица измерения',
            dataIndex: 'unitMeasure',
            key: 'unitMeasure',
        },
        {
            title: 'Количество',
            dataIndex: 'value',
            key: 'value',
        },
        {
            title: 'Дейсвие',
            key: 'action',
            render: (cell, row, index) => (
                <Space size="middle">
                    <a onClick={() => operation("PLUS", row)}>Добавить</a>
                    <a onClick={() => operation("MINUS", row)}>Списать</a>
                </Space>
            ),
        },
    ]

    return (
        <div>
            <Modal title="Списание/Добавление материала" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
                <Input onChange={(value) => onHandleInput('VALUE', value)}></Input>
            </Modal>

            <Table key={uuid()} bordered={true} rowKey={(record) => record.id} columns={columns} size="small" dataSource={adminStore.getMaterials}  ></Table>
            <Row>
                <Col span={12}>
                    <Form>
                        <Form.Item label="Название материала">
                            <Input onChange={(value) => onHandleInput("MATERIAL_NAME", value)}></Input>
                        </Form.Item>
                        <Form.Item>
                            <Select onChange={(value) => onHandleInput("SELECT_MEASURE", value)}>
                                <Option value="мл">Милилитры</Option>
                                <Option value="грамм">Граммы</Option>
                                <Option value="шт">Штуки</Option>
                                <Option value="др.">Другое</Option>
                            </Select>
                        </Form.Item>
                        <Form.Item label="Количество">
                            <Input onChange={(value) => onHandleInput("MATERIAL_COUNT", value)}></Input>
                        </Form.Item>
                        <Button type="primary" onClick={() => addMaterial()} >Добавить материал</Button>
                    </Form>
                </Col>
            </Row>
            <Divider></Divider>
        </div>);
});

export default Store;
import React, { Component, useState, useContext, useEffect } from 'react';
import { Carousel, Typography, Upload, Divider, message, Table, Image, Space, Checkbox, Form, Input, Button, Select } from 'antd';
import AdminContext from '../../contexts/AdminContext';
import { observer } from "mobx-react";
import uuid from 'react-uuid';
import ImgCrop from 'antd-img-crop';
import { UploadOutlined } from '@ant-design/icons';
import { API_BASE_IMAGE_URL } from '../../constants/index';
import {getUploadUrl} from '../../utils/Api';
const { TextArea } = Input;


const onPreview = async file => {
    let src = file.url;
    if (!src) {
        src = await new Promise(resolve => {
            const reader = new FileReader();
            reader.readAsDataURL(file.originFileObj);
            reader.onload = () => resolve(reader.result);
        });
    }
    const image = new Image();
    image.src = src;
    const imgWindow = window.open(src);
    imgWindow.document.write(image.outerHTML);
};


const MastersEdits = observer(() => {
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);
    const [selectedItems, setSelectedItems] = useState([]);
    const [master, setMaster] = useState({});
    const adminStore = useContext(AdminContext);
    useEffect(() => reload(), []);
    const reload = () => { adminStore.loadUsers(); }
    const [form] = Form.useForm();
    const [fileList, setFileList] = useState([{
        uid: '-1',
        name: 'image.png',
        status: 'done',
        url: '',
    },
    ]);


    const getSelectedRow = (selectedRowKeys, selectedRows) => {
        try {
            if (selectedRows === undefined) return;
            const sRow = Object.assign({}, selectedRows[0]);
            setMaster(sRow);
            form.setFieldsValue(sRow);
            setFileList([{ url: "" }])
            setSelectedItems(master.skills.map(x => x.id));
        } catch (e) { console.log(e) }
    }

    const rowSelection = {
        onChange: getSelectedRow,
        type: 'radio'
    }
    const onFinish = (values) => {
        master.name = values.name;
        master.username = values.username;
        master.description = values.description;
        master.active = values.active;
        console.log("Master ", JSON.stringify(master));
        adminStore.editMaster(master);
    };


    const handleChange = selectedItems => {
        try {
            console.log(selectedItems);
            master.skills = [];
            master.skills = adminStore.getServices.filter(x => selectedItems.includes(x.id));
            setSelectedItems(selectedItems);
        } catch (e) { console.log(e); }
    }

    const columns = [
        {
            title: '#????????????????????????',
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: '??????',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: '?????? ????????????????????????',
            dataIndex: 'username',
            key: 'username',
        },
    ]


    const props = {
        name: 'file',
        action: getUploadUrl(),
        headers: {
            authorization: 'authorization-text',
        },
        onChange(info) {
            if (info.file.status !== 'uploading') {
                console.log(info.file, info.fileList);
            }
            if (info.file.status === 'done') {
                message.success(`${info.file.name} file uploaded successfully`);
                master.photoSrc=info.file.response.body;
            } else if (info.file.status === 'error') {
                message.error(`${info.file.name} file upload failed.`);
            }
        },
    };

    //debugger

    return (
        <div>
            <Table bordered={true} rowKey={(record) => record.id} rowSelection={rowSelection} columns={columns} size="small" dataSource={adminStore.getMasters}  ></Table>
            <h4>?????????????????? ????????????</h4>
            <Form form={form} onFinish={onFinish} key={uuid()} formLayout='vertical' >
                <Form.Item style={{ display: 'none' }} name="id">
                    <Input />
                </Form.Item>
                <Form.Item style={{ display: 'none' }} name="photoSrc">
                    <Input />
                </Form.Item>
                <Form.Item label="?????? ????????????????????????" name="name">
                    <Input />
                </Form.Item>
                <Form.Item label="?????? ????????????????????????" name="username" >
                    <Input />
                </Form.Item>
                <Form.Item valuePropName="checked" name="active" >
                    <Checkbox> ???????????? ????????????????</Checkbox>
                </Form.Item>
                <Form.Item label="???????????????? ??????????????" name="description">
                    <TextArea rows={4} />
                </Form.Item>
                <Form.Item
                    label="???????????? ?????????????????????????????? ????????????????"
                >
                    <Select
                        mode="multiple"
                        placeholder="C?????????? ??????????"
                        onChange={handleChange}
                        showSearch
                        allowClear
                        value={selectedItems}
                    >
                        {adminStore.getServices.map(item => (
                            <Select.Option key={uuid()} value={item.id}>
                                {item.name}
                            </Select.Option>
                        ))}
                    </Select>
                </Form.Item>
                <Form.Item name="imageload">
                    <h4>???????????????????? ??????????????</h4>
                    <Image
                        width={200}
                        src={API_BASE_IMAGE_URL + master.photoSrc}
                    />
                    <br></br>
                    <ImgCrop rotate>
                        <Upload {...props}>
                            <Button icon={<UploadOutlined />}>????????????????</Button>
                        </Upload>
                    </ImgCrop>
                </Form.Item>
                <Form.Item >
                    <Button type="primary" htmlType="submit">??????????????????</Button>
                </Form.Item>
            </Form>
            <Divider></Divider>
        </div>);
});

export default MastersEdits;
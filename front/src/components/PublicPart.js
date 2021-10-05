import React, { useState } from 'react';
import { Layout, Menu, Row, Col } from 'antd';
import { InstagramOutlined, UserOutlined } from '@ant-design/icons';
import uuid from 'react-uuid';
import FirstPage from './public/FirstPage';
import Service from './public/Service';
import DiscountList from './public/DiscountList';
import Orders from './public/Order';
import Masters from './public/Masters';
import Cabinet from './Cabinet';
import {
    Link
} from "react-router-dom";
import PublicContext from "../contexts/PublicContext";
import publicStore from "../stores/PublicStore";

const { Footer,  Content } = Layout;
const PublicPart = () => {
    const [content, setContent] = useState([<FirstPage key={uuid()}></FirstPage>]);
    const chooseElement = (type) => {
        switch (type) {
            case "discount": { setContent([<DiscountList key={uuid()}></DiscountList>]); break; }
            case "masters": { setContent([<Masters key={uuid()}></Masters>]); break; }
            case "services": { setContent([<Service key={uuid()}></Service>]); break; }
            case "cabinet": { setContent([<Cabinet key={uuid()}></Cabinet>]); break; }
            case "order": { setContent([<Orders key={uuid()}></Orders>]); break; }
            default: { setContent([<FirstPage key="4"></FirstPage>]) }
        }
    }

    return (
        <PublicContext.Provider value={publicStore}>
            <Row>
                <Col span={17} >
                    <Menu theme="dark" mode="horizontal" >
                        <Menu.Item key="1"></Menu.Item>
                        <Menu.Item key="2"> <Link to="/">Салон Красоты </Link></Menu.Item>
                    </Menu>
                </Col>
                <Col span={3} >
                    <Menu theme="dark" mode="horizontal" >
                        <Menu.Item key="3" onClick={() => chooseElement("cabinet")} ><UserOutlined />Личный кабинет</Menu.Item>
                    </Menu>
                </Col>
                <Col span={4} >
                    <Menu theme="dark" mode="horizontal" >
                        <Menu.Item key="3"><InstagramOutlined /><a href="https://www.instagram.com/sm_centr"> Наш инстраграм</a></Menu.Item>
                    </Menu>
                </Col>
            </Row>
            <Content style={{ padding: '0 50px' }}>
                <Menu theme="light" mode="horizontal" >
                    <Menu.Item key="3" onClick={() => chooseElement("services")} >Услуги</Menu.Item>
                    <Menu.Item key="4" onClick={() => chooseElement("masters")}>Мастера</Menu.Item>
                    <Menu.Item key="5" onClick={() => chooseElement("discount")}>Акции и скидки</Menu.Item>
                    <Menu.Item key="6" onClick={() => chooseElement("order")}>Записаться</Menu.Item>
                </Menu>
                <div className="site-layout-content">
                    {content}
                </div>
            </Content>
            <Footer style={{ textAlign: 'center' }}>©2021 Создано в рамках дипломного проектирования </Footer>
        </PublicContext.Provider>
    );
}
export default PublicPart;
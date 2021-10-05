import React, {  useState } from 'react';
import { Menu, Row, Col, PageHeader, Drawer, Anchor, Typography } from 'antd';
import { useHistory } from "react-router-dom";
import ServiceEdits from './admin/ServiceEdit';
import MastersEdits from './admin/MastersEdit';
import Store from './admin/Store';
import AdminContext from '../contexts/AdminContext';
import AdminStore from '../stores/AdminStore';
import DiscountEdit from './admin/DiscountEdit';
import Booking from './admin/Booking';
import Cashier from './admin/Cashier';
import PayMaster from './admin/PayMaster';
import Users from './admin/Users';
import Stats from './admin/Stats';
const { Title } = Typography;

const { Link } = Anchor;
const AdminPart = () => {
    const [visible, setVisible] = useState(false);
    const navigator = useHistory();

    const showDrawer = () => {
        setVisible(true);
    };

    const onClose = () => {
        setVisible(false);
    };
    const logOff = () => {
        navigator.go(0);
    }

    return (
        <AdminContext.Provider value={AdminStore}>
            <div>
                <Row>
                    <Col span={24} >
                        <Menu theme="dark" mode="horizontal" >
                            <Menu.Item key="1" onClick={showDrawer}> Навигация</Menu.Item>
                            <Menu.Item key="2" onClick={()=>logOff()}>Главная</Menu.Item>
                        </Menu>
                    </Col>
                </Row>
                <Row>
                    <Col span={22} offset={1} >
                        <PageHeader
                            className="site-page-header"
                            onBack={() => null}
                            title="Часть администратора"
                            subTitle="Тут находятся элементы панели администратора"
                        />
                        <Drawer
                            title="Меню навигации"
                            placement='left'
                            closable={false}
                            onClose={onClose}
                            visible={visible}

                        >
                            <Anchor>
                                <Link href="#service" title="Услуги"></Link>
                                <Link href="#masters" title="Мастера"></Link>
                                <Link href="#users" title="Пользователи"></Link>
                                <Link href="#store" title="Работа со складом"></Link>
                                <Link href="#discount" title="Скидки"></Link>
                                <Link href="#booking" title="Записи"></Link>
                                <Link href="#cashier" title="Касса"></Link>
                                <Link href="#pay" title="Расчет платы мастера"></Link>
                                <Link href="#static" title="Стастистика"></Link>
                            </Anchor>
                        </Drawer>
                        <Title level={3}>Список услуг и их редактирование</Title>
                        <a name="service"></a>
                        <ServiceEdits></ServiceEdits>
                        <Title level={3}>Список мастеров</Title>
                        <a name="masters"></a>
                        <MastersEdits></MastersEdits>
                        <a name="users"></a>
                        <Title level={3}>Пользователи</Title>
                        <Users></Users>
                        <Title level={3}>Материалы на складе</Title>
                        <a name="store"></a>
                        <Store></Store>

                        <a name="discount" ></a>
                        <Title level={3}>Список скидок</Title>
                        <DiscountEdit></DiscountEdit>

                        <a name="booking"></a>
                        <Title level={3}>Работа над записями</Title>
                        <Booking></Booking>

                        <a name="cashier"></a>
                        <Title level={3}>Касса</Title>
                        <Cashier></Cashier>

                        <a name="cashier"></a>
                        <Title level={3}>Расчет платы мастера</Title>
                        <PayMaster></PayMaster>
                        <a name="static"></a>
                        <Title level={3}>Стастистика</Title>
                        <Stats></Stats>
                    </Col>
                </Row>

            </div>
        </AdminContext.Provider>
    );
}

export default AdminPart;
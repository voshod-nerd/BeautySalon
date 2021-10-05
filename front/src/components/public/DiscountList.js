import React, { Component,useContext,useEffect } from 'react';
import { Carousel, Image, Typography, Divider,Row,Col,Card } from 'antd';
import { observer } from "mobx-react";
import PublicContext from "../../contexts/PublicContext";
const { Title } = Typography;

const DiscountList = observer(() => {
    const publicStore = useContext(PublicContext);
    useEffect(() => publicStore.loadDiscounts(), []);

    return (
        <div>
            {publicStore.getDiscount.map((item) =>
                <div key={item.id}>
                    <Card title={"Уровень "+item.name} bordered={true}>
                        <Row>
                            <Col span={20}>
                                <Title level={5}>{item.description}</Title>
                            </Col>
                        </Row>
                    </Card>
                </div>
            )}
        </div>);
});

export default DiscountList;
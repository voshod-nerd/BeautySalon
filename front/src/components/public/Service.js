import React, { useContext, useEffect } from 'react';
import { Typography, Card, Row, Col } from 'antd';
import { observer } from "mobx-react";
import PublicContext from "../../contexts/PublicContext";
const { Title } = Typography;

const CardMaster = (props) => {
    const list = props.services;
    return (
        <div>
            {list.map((item) =>
                <div key={item.id}>
                    <Card title={item.name} bordered={true}>
                        <Row>
                            <Col span={20}>
                                <Title level={5}>{item.description}</Title>
                            </Col>
                        </Row>
                    </Card>
                </div>
            )}
        </div>
    );
}

const Service = observer(() => {
    const publicStore = useContext(PublicContext);
    useEffect(() => publicStore.loadServices(),[]);

    return (
        <div>
            <div >
                <CardMaster services={publicStore.serviceList} ></CardMaster>
            </div>
        </div>);
});

export default Service;
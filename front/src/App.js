import React from 'react';
import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import { ConfigProvider } from 'antd';
import ru_RU from 'antd/lib/locale/ru_RU';

import MasterPart from './components/MasterPart';
import AdminPart from './components/AdminPart';
import PublicPart from './components/PublicPart';

const App = () => {
  return (
    <Router>
      <Switch>
        <ConfigProvider locale={ru_RU}>
          <Route path="/admin" component={AdminPart} />
          <Route path="/master" component={MasterPart} />
          <Route path="/public" component={PublicPart} />
          <Redirect to="/public"></Redirect>
        </ConfigProvider>
      </Switch>
    </Router>
  );
}
export default App;



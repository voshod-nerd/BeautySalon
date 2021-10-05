import React from 'react';
import MasterPage from './master/MasterPage';
import MasterStore from '../stores/MasterStore';
import MasterContext from '../contexts/MasterContext';

const MasterPart = () => {
    return (
        <MasterContext.Provider value={MasterStore}>
            <MasterPage></MasterPage>
        </MasterContext.Provider>);
}

export default MasterPart;
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter } from 'react-router-dom';
import { UserContextProvider } from './context/UserContext';
import { RoomContextProvider } from './context/RoomContext';
import { AppContextProvider } from './context/AppContext';

// const { user } = UserContext(UserContext);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <AppContextProvider>
                {/* <RoomContextProvider> */}
                {/* <UserContextProvider> */}
                <App />
                {/* </UserContextProvider> */}
                {/* </RoomContextProvider> */}
            </AppContextProvider>
        </BrowserRouter>
    </React.StrictMode>,
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

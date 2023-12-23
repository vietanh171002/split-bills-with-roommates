import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import './App.scss';
import { privateRoutes, publicRoutes } from './routes/routes';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function App() {
    return (
        <>
            <Header />
            <div className="body">
                <Routes>
                    {privateRoutes.map((route, index) => (
                        <Route key={index} path={route.path} element={route.element} />
                    ))}

                    {publicRoutes.map((route, index) => (
                        <Route key={index} path={route.path} element={route.element} />
                    ))}
                </Routes>
            </div>
            <ToastContainer />
        </>
    );
}

export default App;

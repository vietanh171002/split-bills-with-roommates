import Expenses from '../pages/Expenses';
import Members from '../pages/Members';
import Home from '../pages/Home';
import Register from '../pages/Register';
import Login from '../pages/Login';
const privateRoutes = [
    { path: '/', element: <Home /> },
    { path: '/expenses', element: <Expenses /> },
    { path: '/members', element: <Members /> },
];

const publicRoutes = [
    { path: '/register', element: <Register /> },
    { path: '/login', element: <Login /> },
];

export { privateRoutes, publicRoutes };

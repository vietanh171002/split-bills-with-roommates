import axios from 'axios';
import { toast } from 'react-toastify';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
});

api.interceptors.request.use(
    (config) => {
        console.log('request>>>', config);
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

api.interceptors.response.use(
    function (response) {
        console.log('res...', response);
        if (response.status === 204) {
            return response.status;
        }
        return response.data;
    },
    function (error) {
        if (error.message && error.message === 'Network Error') {
            toast.error('Network error');
            return;
        }
        if (error.response.status === 401) {
            toast.error('Session expired, please log in again');
            localStorage.removeItem('token');
            window.location.reload();
            return;
        }
        if (error.response.status === 403) {
            localStorage.removeItem('token');
            window.location.reload();
            return;
        }
        if (error.response.data) {
            toast.error(error.response.data);
            return;
        }
        if (!error.response.data) {
            toast.error('Error occurs');
            return;
        }
        return;
    },
);

export default api;

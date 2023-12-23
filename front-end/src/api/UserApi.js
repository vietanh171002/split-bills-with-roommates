import api from './axios-custom';

export const register = (name, email, password) => {
    return api.post('/user/register', { name, email, password });
};

export const login = (email, password) => {
    return api.post('/user/login', { email, password });
};

export const getUserInfo = () => {
    return api.get('/user/info');
};

export const changePassword = (oldPassword, newPassword) => {
    return api.put('/user/change-password', { oldPassword, newPassword });
};

export const editUserInfo = (newName, newEmail) => {
    return api.put('/user/edit-info', { newName, newEmail });
};

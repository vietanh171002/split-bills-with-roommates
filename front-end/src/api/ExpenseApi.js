import api from './axios-custom';
export const addExpense = (roomId, expense) => {
    return api.post(`/spending/create/${roomId}`, expense);
};

export const editExpense = (spendingId, expense) => {
    return api.put(`/spending/${spendingId}/edit`, expense);
};

export const deleteExpense = (spendingId) => {
    return api.delete(`/spending/${spendingId}/delete-spending`);
};

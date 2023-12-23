import api from './axios-custom';

export const getRoomData = (roomId) => {
    return api.get(`/room/${roomId}/info`);
};

export const createRoom = (roomName) => {
    return api.post('/room/create', { roomName });
};

export const editRoomName = (roomId, roomName) => {
    return api.put(`/room/${roomId}/edit-room-name`, { roomName });
};

export const deleteRoom = (roomId) => {
    return api.delete(`/room/${roomId}/delete`);
};

export const addUserToRoom = (roomId, addedUserEmail) => {
    return api.post(`/room/${roomId}/add-member-by-email/${addedUserEmail}`);
};

export const changeRoomOwner = (roomId, newOwnerId) => {
    return api.put(`/room/${roomId}/change-room-owner/${newOwnerId}`);
};

export const removeMember = (roomId, removedMemberId) => {
    return api.delete(`/room/${roomId}/remove-member/${removedMemberId}`);
};

export const leaveRoom = (roomId) => {
    return api.put(`/room/${roomId}/leave`);
};

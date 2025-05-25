import api from './api';

const userService = {
  getUsers: async () => {
    const response = await api.get('/users');
    return response.data;
  },

  getUserById: async (id) => {
    const response = await api.get(`/users/${id}`);
    return response.data;
  },

  updateUser: async (id, updates) => {
    const response = await api.patch(`/users/${id}`, updates);
    return response.data;
  },

  updateUserTotally: async (id, userData) => {
    const response = await api.put(`/users/${id}`, userData);
    return response.data;
  },

  deleteUser: async (id) => {
    const response = await api.delete(`/users/${id}`);
    return response.data;
  },

  getUserQueues: async (id) => {
    const response = await api.get(`/users/${id}/queues`);
    return response.data;
  },
};

export default userService;
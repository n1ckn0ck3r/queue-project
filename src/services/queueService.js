import api from './api';

const queueService = {
  getQueues: async () => {
    const response = await api.get('/queues');
    return response.data;
  },

  getQueueById: async (id) => {
    const response = await api.get(`/queues/${id}`);
    return response.data;
  },

  createQueue: async (queueData) => {
    const response = await api.post('/queues', queueData);
    return response.data;
  },

  updateQueue: async (id, updates) => {
    const response = await api.patch(`/queues/${id}`, updates);
    return response.data;
  },

  deleteQueue: async (id) => {
    const response = await api.delete(`/queues/${id}`);
    return response.data;
  },

  getQueueUsers: async (id) => {
    const response = await api.get(`/queues/${id}/users`);
    return response.data;
  },

  addUsersToQueue: async (queueId, userIds) => {
    const response = await api.post(`/queues/${queueId}/users`, { data: userIds });
    return response.data;
  },

  removeUsersFromQueue: async (queueId, userIds) => {
    const response = await api.delete(`/queues/${queueId}/users`, { data: { data: userIds } });
    return response.data;
  },

  addUserToQueue: async (queueId, userId) => {
    const response = await api.post(`/queues/${queueId}/users/${userId}`);
    return response.data;
  },

  removeUserFromQueue: async (queueId, userId) => {
    const response = await api.delete(`/queues/${queueId}/users/${userId}`);
    return response.data;
  },
};

export default queueService;
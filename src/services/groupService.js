import api from './api';

const groupService = {
  getGroups: async () => {
    const response = await api.get('/groups');
    return response.data;
  },

  getGroupById: async (id) => {
    const response = await api.get(`/groups/${id}`);
    return response.data;
  },

  createGroup: async (groupData) => {
    const response = await api.post('/groups', groupData);
    return response.data;
  },

  updateGroup: async (id, updates) => {
    const response = await api.patch(`/groups/${id}`, updates);
    return response.data;
  },

  deleteGroup: async (id) => {
    const response = await api.delete(`/groups/${id}`);
    return response.data;
  },
};

export default groupService;
import api from './api';

const disciplineService = {
  getDisciplines: async () => {
    const response = await api.get('/disciplines');
    return response.data;
  },

  getDisciplineById: async (id) => {
    const response = await api.get(`/disciplines/${id}`);
    return response.data;
  },

  createDiscipline: async (disciplineData) => {
    const response = await api.post('/disciplines', disciplineData);
    return response.data;
  },

  updateDiscipline: async (id, updates) => {
    const response = await api.patch(`/disciplines/${id}`, updates);
    return response.data;
  },

  deleteDiscipline: async (id) => {
    const response = await api.delete(`/disciplines/${id}`);
    return response.data;
  },
};

export default disciplineService;
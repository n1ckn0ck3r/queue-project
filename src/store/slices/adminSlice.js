import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import groupService from '../../services/groupService';
import disciplineService from '../../services/disciplineService';

// Async thunks for groups
export const fetchGroups = createAsyncThunk(
  'admin/fetchGroups',
  async (_, { rejectWithValue }) => {
    try {
      return await groupService.getGroups();
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const createGroup = createAsyncThunk(
  'admin/createGroup',
  async (groupData, { rejectWithValue }) => {
    try {
      return await groupService.createGroup(groupData);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const updateGroup = createAsyncThunk(
  'admin/updateGroup',
  async ({ id, updates }, { rejectWithValue }) => {
    try {
      return await groupService.updateGroup(id, updates);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const deleteGroup = createAsyncThunk(
  'admin/deleteGroup',
  async (id, { rejectWithValue }) => {
    try {
      return await groupService.deleteGroup(id);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

// Async thunks for disciplines
export const fetchDisciplines = createAsyncThunk(
  'admin/fetchDisciplines',
  async (_, { rejectWithValue }) => {
    try {
      return await disciplineService.getDisciplines();
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const createDiscipline = createAsyncThunk(
  'admin/createDiscipline',
  async (disciplineData, { rejectWithValue }) => {
    try {
      return await disciplineService.createDiscipline(disciplineData);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const updateDiscipline = createAsyncThunk(
  'admin/updateDiscipline',
  async ({ id, updates }, { rejectWithValue }) => {
    try {
      return await disciplineService.updateDiscipline(id, updates);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const deleteDiscipline = createAsyncThunk(
  'admin/deleteDiscipline',
  async (id, { rejectWithValue }) => {
    try {
      return await disciplineService.deleteDiscipline(id);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

const initialState = {
  groups: [],
  disciplines: [],
  isLoading: false,
  error: null,
};

const adminSlice = createSlice({
  name: 'admin',
  initialState,
  reducers: {
    resetAdminState: (state) => {
      state.error = null;
      state.isLoading = false;
    },
  },
  extraReducers: (builder) => {
    builder
      // Group reducers
      .addCase(fetchGroups.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchGroups.fulfilled, (state, action) => {
        state.isLoading = false;
        state.groups = action.payload;
      })
      .addCase(fetchGroups.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      .addCase(createGroup.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(createGroup.fulfilled, (state, action) => {
        state.isLoading = false;
        state.groups.push(action.payload);
      })
      .addCase(createGroup.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      .addCase(updateGroup.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(updateGroup.fulfilled, (state, action) => {
        state.isLoading = false;
        const index = state.groups.findIndex(group => group.id === action.payload.id);
        if (index !== -1) {
          state.groups[index] = action.payload;
        }
      })
      .addCase(updateGroup.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      .addCase(deleteGroup.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(deleteGroup.fulfilled, (state, action) => {
        state.isLoading = false;
        state.groups = state.groups.filter(group => group.id !== action.payload.id);
      })
      .addCase(deleteGroup.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Discipline reducers
      .addCase(fetchDisciplines.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchDisciplines.fulfilled, (state, action) => {
        state.isLoading = false;
        state.disciplines = action.payload;
      })
      .addCase(fetchDisciplines.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      .addCase(createDiscipline.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(createDiscipline.fulfilled, (state, action) => {
        state.isLoading = false;
        state.disciplines.push(action.payload);
      })
      .addCase(createDiscipline.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      .addCase(updateDiscipline.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(updateDiscipline.fulfilled, (state, action) => {
        state.isLoading = false;
        const index = state.disciplines.findIndex(discipline => discipline.id === action.payload.id);
        if (index !== -1) {
          state.disciplines[index] = action.payload;
        }
      })
      .addCase(updateDiscipline.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      .addCase(deleteDiscipline.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(deleteDiscipline.fulfilled, (state, action) => {
        state.isLoading = false;
        state.disciplines = state.disciplines.filter(discipline => discipline.id !== action.payload.id);
      })
      .addCase(deleteDiscipline.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      });
  },
});

export const { resetAdminState } = adminSlice.actions;
export default adminSlice.reducer;
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import userService from '../../services/userService';

// Async thunks
export const fetchUserProfile = createAsyncThunk(
  'profile/fetchUserProfile',
  async (id, { rejectWithValue }) => {
    try {
      return await userService.getUserById(id);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const updateUserProfile = createAsyncThunk(
  'profile/updateUserProfile',
  async ({ id, updates }, { rejectWithValue }) => {
    try {
      return await userService.updateUser(id, updates);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const fetchUserQueues = createAsyncThunk(
  'profile/fetchUserQueues',
  async (id, { rejectWithValue }) => {
    try {
      return await userService.getUserQueues(id);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

const initialState = {
  profile: null,
  userQueues: [],
  isLoading: false,
  error: null,
};

const profileSlice = createSlice({
  name: 'profile',
  initialState,
  reducers: {
    resetProfileState: (state) => {
      state.error = null;
      state.isLoading = false;
    },
    clearProfile: (state) => {
      state.profile = null;
      state.userQueues = [];
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch user profile
      .addCase(fetchUserProfile.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchUserProfile.fulfilled, (state, action) => {
        state.isLoading = false;
        state.profile = action.payload;
      })
      .addCase(fetchUserProfile.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Update user profile
      .addCase(updateUserProfile.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(updateUserProfile.fulfilled, (state, action) => {
        state.isLoading = false;
        state.profile = action.payload;

        const user = JSON.parse(localStorage.getItem('user'));
        if (user) {
          const updatedUser = { ...user, ...action.payload }
          localStorage.setItem('user', JSON.stringify(user));
        }
      })
      .addCase(updateUserProfile.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Fetch user queues
      .addCase(fetchUserQueues.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchUserQueues.fulfilled, (state, action) => {
        state.isLoading = false;
        state.userQueues = action.payload;
      })
      .addCase(fetchUserQueues.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      });
  },
});

export const { resetProfileState, clearProfile } = profileSlice.actions;
export default profileSlice.reducer;
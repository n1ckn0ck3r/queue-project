import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import queueService from '../../services/queueService';

// Async thunks
export const fetchQueues = createAsyncThunk(
  'queue/fetchQueues',
  async (_, { rejectWithValue }) => {
    try {
      return await queueService.getQueues();
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const fetchQueueById = createAsyncThunk(
  'queue/fetchQueueById',
  async (id, { rejectWithValue }) => {
    try {
      return await queueService.getQueueById(id);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const createQueue = createAsyncThunk(
  'queue/createQueue',
  async (queueData, { rejectWithValue }) => {
    try {
      return await queueService.createQueue(queueData);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const updateQueue = createAsyncThunk(
  'queue/updateQueue',
  async ({ id, updates }, { rejectWithValue }) => {
    try {
      return await queueService.updateQueue(id, updates);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const deleteQueue = createAsyncThunk(
  'queue/deleteQueue',
  async (id, { rejectWithValue }) => {
    try {
      return await queueService.deleteQueue(id);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const fetchQueueUsers = createAsyncThunk(
  'queue/fetchQueueUsers',
  async (id, { rejectWithValue }) => {
    try {
      return await queueService.getQueueUsers(id);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const addUserToQueue = createAsyncThunk(
  'queue/addUserToQueue',
  async ({ queueId, userId }, { rejectWithValue }) => {
    try {
      return await queueService.addUserToQueue(queueId, userId);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

export const removeUserFromQueue = createAsyncThunk(
  'queue/removeUserFromQueue',
  async ({ queueId, userId }, { rejectWithValue }) => {
    try {
      return await queueService.removeUserFromQueue(queueId, userId);
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: error.message });
    }
  }
);

const initialState = {
  queues: [],
  currentQueue: null,
  queueUsers: [],
  isLoading: false,
  error: null,
};

const queueSlice = createSlice({
  name: 'queue',
  initialState,
  reducers: {
    resetQueueState: (state) => {
      state.error = null;
      state.isLoading = false;
    },
    resetCurrentQueue: (state) => {
      state.currentQueue = null;
      state.queueUsers = [];
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch queues
      .addCase(fetchQueues.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchQueues.fulfilled, (state, action) => {
        state.isLoading = false;
        state.queues = action.payload;
      })
      .addCase(fetchQueues.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Fetch queue by ID
      .addCase(fetchQueueById.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchQueueById.fulfilled, (state, action) => {
        state.isLoading = false;
        state.currentQueue = action.payload;
      })
      .addCase(fetchQueueById.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Create queue
      .addCase(createQueue.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(createQueue.fulfilled, (state, action) => {
        state.isLoading = false;
        state.queues.push(action.payload);
      })
      .addCase(createQueue.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Update queue
      .addCase(updateQueue.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(updateQueue.fulfilled, (state, action) => {
        state.isLoading = false;
        const index = state.queues.findIndex(queue => queue.id === action.payload.id);
        if (index !== -1) {
          state.queues[index] = action.payload;
        }
        if (state.currentQueue && state.currentQueue.id === action.payload.id) {
          state.currentQueue = action.payload;
        }
      })
      .addCase(updateQueue.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Delete queue
      .addCase(deleteQueue.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(deleteQueue.fulfilled, (state, action) => {
        state.isLoading = false;
        state.queues = state.queues.filter(queue => queue.id !== action.payload.id);
        if (state.currentQueue && state.currentQueue.id === action.payload.id) {
          state.currentQueue = null;
        }
      })
      .addCase(deleteQueue.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Fetch queue users
      .addCase(fetchQueueUsers.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchQueueUsers.fulfilled, (state, action) => {
        state.isLoading = false;
        state.queueUsers = action.payload;
      })
      .addCase(fetchQueueUsers.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Add user to queue
      .addCase(addUserToQueue.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(addUserToQueue.fulfilled, (state, action) => {
        state.isLoading = false;
        // Update queue users if we're viewing the queue that was updated
        if (state.currentQueue && state.currentQueue.id === action.payload.queueId) {
          // Refresh queue users (this is simplified, in a real app you might want to add the user to the list)
          // For simplicity, we'll assume fetchQueueUsers will be called after this action
        }
      })
      .addCase(addUserToQueue.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })
      // Remove user from queue
      .addCase(removeUserFromQueue.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(removeUserFromQueue.fulfilled, (state, action) => {
        state.isLoading = false;
        // Update queue users if we're viewing the queue that was updated
        if (state.currentQueue && state.currentQueue.id === action.payload.queueId) {
          // Refresh queue users (this is simplified, in a real app you might want to remove the user from the list)
          // For simplicity, we'll assume fetchQueueUsers will be called after this action
        }
      })
      .addCase(removeUserFromQueue.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      });
  },
});

export const { resetQueueState, resetCurrentQueue } = queueSlice.actions;
export default queueSlice.reducer;
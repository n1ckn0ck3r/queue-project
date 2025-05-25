import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { 
  fetchQueueById, 
  fetchQueueUsers, 
  updateQueue, 
  deleteQueue, 
  addUserToQueue, 
  removeUserFromQueue,
  resetQueueState 
} from '../store/slices/queueSlice';
import { openModal, closeModal } from '../store/slices/uiSlice';

const QueueDetailPage = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { currentQueue, queueUsers, isLoading, error } = useSelector((state) => state.queue);
  const { modal } = useSelector((state) => state.ui);
  const { user } = useSelector((state) => state.auth);
  
  useEffect(() => {
    dispatch(resetQueueState());
    dispatch(fetchQueueById(id));
    dispatch(fetchQueueUsers(id));
  }, [dispatch, id]);

  const handleJoinQueue = () => {
    if (user && currentQueue) {
      dispatch(addUserToQueue({ queueId: currentQueue.id, userId: user.id }))
        .then(() => {
          dispatch(fetchQueueUsers(id));
        });
    }
  };

  const handleLeaveQueue = () => {
    if (user && currentQueue) {
      dispatch(removeUserFromQueue({ queueId: currentQueue.id, userId: user.id }))
        .then(() => {
          dispatch(fetchQueueUsers(id));
        });
    }
  };

  const handleEditQueue = () => {
    dispatch(openModal({ type: 'EDIT_QUEUE', data: currentQueue }));
  };

  const handleDeleteQueue = () => {
    if (window.confirm('Are you sure you want to delete this queue?')) {
      dispatch(deleteQueue(id))
        .then(() => {
          navigate('/queues');
        });
    }
  };

  const handleCloseModal = () => {
    dispatch(closeModal());
  };

  const isUserInQueue = () => {
    return queueUsers.some(queueUser => queueUser.id === user?.id);
  };

  if (isLoading && !currentQueue) {
    return (
      <div className="spinner-container">
        <div className="spinner"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-error">
        {error.message || 'Failed to load queue details. Please try again.'}
      </div>
    );
  }

  if (!currentQueue) {
    return (
      <div className="alert alert-error">
        Queue not found.
      </div>
    );
  }

  return (
    <div>
      <div className="mb-4">
        <Link to="/queues" className="btn btn-secondary mb-3">
          &larr; Back to Queues
        </Link>
        <div className="d-flex justify-content-between align-items-center">
          <h1>{currentQueue.name}</h1>
          <div>
            {!isUserInQueue() ? (
              <button className="btn btn-primary mr-2" onClick={handleJoinQueue}>
                Join Queue
              </button>
            ) : (
              <button className="btn btn-danger mr-2" onClick={handleLeaveQueue}>
                Leave Queue
              </button>
            )}
            <button className="btn btn-secondary mr-2" onClick={handleEditQueue}>
              Edit
            </button>
            <button className="btn btn-danger" onClick={handleDeleteQueue}>
              Delete
            </button>
          </div>
        </div>
      </div>

      <div className="queue-detail">
        <div className="mb-4">
          <h3>Queue Information</h3>
          <p><strong>Discipline:</strong> {currentQueue.discipline.disciplineName || 'N/A'}</p>
          <p><strong>Status:</strong> {currentQueue.active ? 'Active' : 'Inactive'}</p>
          <p><strong>Created:</strong> {new Date(currentQueue.queueStart).toLocaleString("ru-RU")}</p>
          <p><strong>Ends:</strong> {new Date(currentQueue.queueEnd).toLocaleString("ru-RU")}</p>
        </div>

        <div className="queue-users">
          <h3>Users in Queue ({queueUsers.length})</h3>
          {queueUsers.length === 0 ? (
            <p>No users in this queue.</p>
          ) : (
            <ul className="user-list">
              {queueUsers.map((user, index) => (
                <li key={user.id} className="user-item">
                  <div>
                    <span className="mr-2">{index + 1}.</span>
                    {user.username}
                  </div>
                  <div>{user.email}</div>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>

      {modal.isOpen && modal.type === 'EDIT_QUEUE' && (
        <div className="modal-backdrop">
          <div className="modal-content">
            <div className="modal-header">
              <h2>Edit Queue</h2>
              <button className="modal-close" onClick={handleCloseModal}>
                &times;
              </button>
            </div>
            <div className="modal-body">
              {/* We'll create this component later */}
              {/* <QueueForm queue={modal.data} onClose={handleCloseModal} /> */}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default QueueDetailPage;
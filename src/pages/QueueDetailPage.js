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
      const now = new Date();
      const queueStart = new Date(currentQueue.queueStart);
      const queueEnd = new Date(currentQueue.queueEnd);

      if (now < queueStart) {
        alert('Очередь еще не началась. Вы не можете присоединиться к ней.');
        return;
      }

      if (now > queueEnd) {
        alert('Очередь уже закончилась. Вы не можете присоединиться к ней.');
        return;
      }

      if (user.user) {
        dispatch(addUserToQueue({ queueId: currentQueue.id, userId: user.user.id }))
          .then(() => dispatch(fetchQueueUsers(id)));
      } else {
        dispatch(addUserToQueue({ queueId: currentQueue.id, userId: user.id }))
          .then(() => dispatch(fetchQueueUsers(id)));
      }
    }
  };

  const handleLeaveQueue = () => {
    if (user && currentQueue) {
      if (user.user) {
        dispatch(removeUserFromQueue({ queueId: currentQueue.id, userId: user.user.id }))
          .then(() => dispatch(fetchQueueUsers(id)));
      } else {
        dispatch(removeUserFromQueue({ queueId: currentQueue.id, userId: user.id }))
          .then(() => dispatch(fetchQueueUsers(id)));
      }
    }
  };

  const handleEditQueue = () => {
    dispatch(openModal({ type: 'EDIT_QUEUE', data: currentQueue }));
  };

  const handleDeleteQueue = () => {
    if (window.confirm('Вы уверены, что хотите удалить эту очередь?')) {
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
    return queueUsers.some(queueUser => (user && user.id && queueUser.id === user.id) || (user && user.user && user.user.id && queueUser.id === user.user.id));
  };

  const isQueueClosed = () => {
    if (!currentQueue) return true;

    const now = new Date();
    const queueStart = new Date(currentQueue.queueStart);
    const queueEnd = new Date(currentQueue.queueEnd);

    return now < queueStart || now > queueEnd;
  };

  const getQueueStatusMessage = () => {
    if (!currentQueue) return '';

    const now = new Date();
    const queueStart = new Date(currentQueue.queueStart);
    const queueEnd = new Date(currentQueue.queueEnd);

    if (now < queueStart) {
      return 'Очередь еще не началась';
    }

    if (now > queueEnd) {
      return 'Очередь уже закончилась';
    }

    return 'Присоединиться к очереди';
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
        {error.message || 'Не удалось загрузить детали очереди. Пожалуйста, попробуйте снова.'}
      </div>
    );
  }

  if (!currentQueue) {
    return (
      <div className="alert alert-error">
        Очередь не найдена.
      </div>
    );
  }

  return (
    <div>
      <div className="mb-4">
        <Link to="/queues" className="btn btn-secondary mb-3">
          &larr; Назад к очередям
        </Link>
        <div className="d-flex justify-content-between align-items-center">
          <h1>{currentQueue.name}</h1>
          <div>
            {!isUserInQueue() ? (
              <button 
                className="btn btn-primary mr-2" 
                onClick={handleJoinQueue}
                disabled={isQueueClosed()}
                title={getQueueStatusMessage()}
              >
                Присоединиться к очереди
              </button>
            ) : (
              <button className="btn btn-danger mr-2" onClick={handleLeaveQueue}>
                Покинуть очередь
              </button>
            )}
            {/* <button className="btn btn-secondary mr-2" onClick={handleEditQueue}> */}
              {/* Редактировать */}
            {/* </button> */}
            <button className="btn btn-danger" onClick={handleDeleteQueue}>
              Удалить
            </button>
          </div>
        </div>
      </div>

      <div className="queue-detail">
        <div className="mb-4">
          <h3>Информация об очереди</h3>
          <p><strong>Дисциплина:</strong> {currentQueue.discipline.disciplineName || 'Н/Д'}</p>
          <p><strong>Статус:</strong> {currentQueue.active ? 'Активна' : 'Неактивна'}</p>
          <p><strong>Создана:</strong> {new Date(currentQueue.queueStart).toLocaleString("ru-RU")}</p>
          <p><strong>Заканчивается:</strong> {new Date(currentQueue.queueEnd).toLocaleString("ru-RU")}</p>
        </div>

        <div className="queue-users">
          <h3>Пользователи в очереди ({queueUsers.length})</h3>
          {queueUsers.length === 0 ? (
            <p>В этой очереди нет пользователей.</p>
          ) : (
            <ul className="user-list">
              {queueUsers.map((user, index) => (
                <li key={user.id} className="user-item">
                  <div>
                    <span className="mr-2">{index + 1}.</span>
                    {user.username}
                  </div>
                  <div className="user-info">
                    <div>{user.email}</div>
                    <div className="join-time">
                      <small>Время входа: {user.joinedAt ? new Date(user.joinedAt).toLocaleString("ru-RU") : 'Н/Д'}</small>
                    </div>
                  </div>
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
              <h2>Редактировать очередь</h2>
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

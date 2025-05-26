import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { fetchQueues, resetQueueState } from '../store/slices/queueSlice';
import { openModal, closeModal } from '../store/slices/uiSlice';
import QueueForm from '../components/QueueForm';

const QueueListPage = () => {
  const dispatch = useDispatch();
  const { queues, isLoading, error } = useSelector((state) => state.queue);
  const { modal } = useSelector((state) => state.ui);

  useEffect(() => {
    dispatch(resetQueueState());
    dispatch(fetchQueues());
  }, [dispatch]);

  const handleCreateQueue = () => {
    dispatch(openModal({ type: 'CREATE_QUEUE' }));
  };

  const handleCloseModal = () => {
    dispatch(closeModal());
  };

  if (isLoading && queues.length === 0) {
    return (
      <div className="spinner-container">
        <div className="spinner"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-error">
        {error.message || 'Не удалось загрузить очереди. Пожалуйста, попробуйте снова.'}
      </div>
    );
  }

  return (
    <div>
      <div className="mb-4 d-flex justify-content-between align-items-center">
        <h1>Очереди</h1>
        <button className="btn btn-primary" onClick={handleCreateQueue}>
          Создать очередь
        </button>
      </div>

      {queues.length === 0 ? (
        <div className="alert alert-info">
          Нет доступных очередей. Создайте новую очередь, чтобы начать.
        </div>
      ) : (
        <div className="queue-list">
          {queues.map((queue) => (
            <div key={queue.id} className="queue-card">
              {/* <h3 className="queue-title">{queue.name}</h3> */}
              <p className="queue-info">
                <strong>Дисциплина:</strong> {queue.discipline.disciplineName || 'Н/Д'}
              </p>
              <p className="queue-info">
                <strong>Статус:</strong> {queue.active ? 'Активна' : 'Неактивна'}
              </p>
              <div className="queue-actions">
                <Link to={`/queues/${queue.id}`} className="btn btn-primary">
                  Просмотр
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}

      {modal.isOpen && modal.type === 'CREATE_QUEUE' && (
        <div className="modal-backdrop">
          <div className="modal-content">
            <div className="modal-header">
              <h2>Создать новую очередь</h2>
              <button className="modal-close" onClick={handleCloseModal}>
                &times;
              </button>
            </div>
            <div className="modal-body">
              <QueueForm onClose={handleCloseModal} />
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default QueueListPage;

import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReportes } from 'app/shared/model/reportes.model';
import { getEntities as getReportes } from 'app/entities/reportes/reportes.reducer';
import { IComentarios } from 'app/shared/model/comentarios.model';
import { getEntity, updateEntity, createEntity, reset } from './comentarios.reducer';

export const ComentariosUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reportes = useAppSelector(state => state.reportes.entities);
  const comentariosEntity = useAppSelector(state => state.comentarios.entity);
  const loading = useAppSelector(state => state.comentarios.loading);
  const updating = useAppSelector(state => state.comentarios.updating);
  const updateSuccess = useAppSelector(state => state.comentarios.updateSuccess);

  const handleClose = () => {
    navigate('/comentarios' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReportes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...comentariosEntity,
      ...values,
      reportes: reportes.find(it => it.id.toString() === values.reportes.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...comentariosEntity,
          reportes: comentariosEntity?.reportes?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="transotasApp.comentarios.home.createOrEditLabel" data-cy="ComentariosCreateUpdateHeading">
            <Translate contentKey="transotasApp.comentarios.home.createOrEditLabel">Create or edit a Comentarios</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="comentarios-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('transotasApp.comentarios.usuario')}
                id="comentarios-usuario"
                name="usuario"
                data-cy="usuario"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.comentario')}
                id="comentarios-comentario"
                name="comentario"
                data-cy="comentario"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra1')}
                id="comentarios-extra1"
                name="extra1"
                data-cy="extra1"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra2')}
                id="comentarios-extra2"
                name="extra2"
                data-cy="extra2"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra3')}
                id="comentarios-extra3"
                name="extra3"
                data-cy="extra3"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra4')}
                id="comentarios-extra4"
                name="extra4"
                data-cy="extra4"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra5')}
                id="comentarios-extra5"
                name="extra5"
                data-cy="extra5"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra6')}
                id="comentarios-extra6"
                name="extra6"
                data-cy="extra6"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra7')}
                id="comentarios-extra7"
                name="extra7"
                data-cy="extra7"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra8')}
                id="comentarios-extra8"
                name="extra8"
                data-cy="extra8"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra9')}
                id="comentarios-extra9"
                name="extra9"
                data-cy="extra9"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.comentarios.extra10')}
                id="comentarios-extra10"
                name="extra10"
                data-cy="extra10"
                type="text"
              />
              <ValidatedField
                id="comentarios-reportes"
                name="reportes"
                data-cy="reportes"
                label={translate('transotasApp.comentarios.reportes')}
                type="select"
              >
                <option value="" key="0" />
                {reportes
                  ? reportes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/comentarios" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ComentariosUpdate;

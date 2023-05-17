import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IIndiceOriginal } from 'app/shared/model/indice-original.model';
import { getEntity, updateEntity, createEntity, reset } from './indice-original.reducer';

export const IndiceOriginalUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const indiceOriginalEntity = useAppSelector(state => state.indiceOriginal.entity);
  const loading = useAppSelector(state => state.indiceOriginal.loading);
  const updating = useAppSelector(state => state.indiceOriginal.updating);
  const updateSuccess = useAppSelector(state => state.indiceOriginal.updateSuccess);

  const handleClose = () => {
    navigate('/indice-original' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.fecha = convertDateTimeToServer(values.fecha);

    const entity = {
      ...indiceOriginalEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fecha: displayDefaultDateTime(),
        }
      : {
          ...indiceOriginalEntity,
          fecha: convertDateTimeFromServer(indiceOriginalEntity.fecha),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="transotasApp.indiceOriginal.home.createOrEditLabel" data-cy="IndiceOriginalCreateUpdateHeading">
            <Translate contentKey="transotasApp.indiceOriginal.home.createOrEditLabel">Create or edit a IndiceOriginal</Translate>
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
                  id="indice-original-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.img')}
                id="indice-original-img"
                name="img"
                data-cy="img"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.titulo')}
                id="indice-original-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.url')}
                id="indice-original-url"
                name="url"
                data-cy="url"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.autor')}
                id="indice-original-autor"
                name="autor"
                data-cy="autor"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.fecha')}
                id="indice-original-fecha"
                name="fecha"
                data-cy="fecha"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.ciudad')}
                id="indice-original-ciudad"
                name="ciudad"
                data-cy="ciudad"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.estado')}
                id="indice-original-estado"
                name="estado"
                data-cy="estado"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.pais')}
                id="indice-original-pais"
                name="pais"
                data-cy="pais"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.comentarios')}
                id="indice-original-comentarios"
                name="comentarios"
                data-cy="comentarios"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.vistas')}
                id="indice-original-vistas"
                name="vistas"
                data-cy="vistas"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indiceOriginal.rating')}
                id="indice-original-rating"
                name="rating"
                data-cy="rating"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/indice-original" replace color="info">
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

export default IndiceOriginalUpdate;

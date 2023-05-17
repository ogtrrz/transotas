import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IIndice } from 'app/shared/model/indice.model';
import { getEntity, updateEntity, createEntity, reset } from './indice.reducer';

export const IndiceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const indiceEntity = useAppSelector(state => state.indice.entity);
  const loading = useAppSelector(state => state.indice.loading);
  const updating = useAppSelector(state => state.indice.updating);
  const updateSuccess = useAppSelector(state => state.indice.updateSuccess);

  const handleClose = () => {
    navigate('/indice' + location.search);
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
      ...indiceEntity,
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
          ...indiceEntity,
          fecha: convertDateTimeFromServer(indiceEntity.fecha),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="transotasApp.indice.home.createOrEditLabel" data-cy="IndiceCreateUpdateHeading">
            <Translate contentKey="transotasApp.indice.home.createOrEditLabel">Create or edit a Indice</Translate>
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
                  id="indice-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('transotasApp.indice.img')} id="indice-img" name="img" data-cy="img" type="text" />
              <ValidatedField
                label={translate('transotasApp.indice.titulo')}
                id="indice-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
              />
              <ValidatedField label={translate('transotasApp.indice.url')} id="indice-url" name="url" data-cy="url" type="text" />
              <ValidatedField label={translate('transotasApp.indice.autor')} id="indice-autor" name="autor" data-cy="autor" type="text" />
              <ValidatedField
                label={translate('transotasApp.indice.fecha')}
                id="indice-fecha"
                name="fecha"
                data-cy="fecha"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('transotasApp.indice.ciudad')}
                id="indice-ciudad"
                name="ciudad"
                data-cy="ciudad"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.estado')}
                id="indice-estado"
                name="estado"
                data-cy="estado"
                type="text"
              />
              <ValidatedField label={translate('transotasApp.indice.pais')} id="indice-pais" name="pais" data-cy="pais" type="text" />
              <ValidatedField
                label={translate('transotasApp.indice.comentarios')}
                id="indice-comentarios"
                name="comentarios"
                data-cy="comentarios"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.vistas')}
                id="indice-vistas"
                name="vistas"
                data-cy="vistas"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.rating')}
                id="indice-rating"
                name="rating"
                data-cy="rating"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra1')}
                id="indice-extra1"
                name="extra1"
                data-cy="extra1"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra2')}
                id="indice-extra2"
                name="extra2"
                data-cy="extra2"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra3')}
                id="indice-extra3"
                name="extra3"
                data-cy="extra3"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra4')}
                id="indice-extra4"
                name="extra4"
                data-cy="extra4"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra5')}
                id="indice-extra5"
                name="extra5"
                data-cy="extra5"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra6')}
                id="indice-extra6"
                name="extra6"
                data-cy="extra6"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra7')}
                id="indice-extra7"
                name="extra7"
                data-cy="extra7"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra8')}
                id="indice-extra8"
                name="extra8"
                data-cy="extra8"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra9')}
                id="indice-extra9"
                name="extra9"
                data-cy="extra9"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.indice.extra10')}
                id="indice-extra10"
                name="extra10"
                data-cy="extra10"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/indice" replace color="info">
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

export default IndiceUpdate;

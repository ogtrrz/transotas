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
import { IInformacion } from 'app/shared/model/informacion.model';
import { getEntity, updateEntity, createEntity, reset } from './informacion.reducer';

export const InformacionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reportes = useAppSelector(state => state.reportes.entities);
  const informacionEntity = useAppSelector(state => state.informacion.entity);
  const loading = useAppSelector(state => state.informacion.loading);
  const updating = useAppSelector(state => state.informacion.updating);
  const updateSuccess = useAppSelector(state => state.informacion.updateSuccess);

  const handleClose = () => {
    navigate('/informacion' + location.search);
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
      ...informacionEntity,
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
          ...informacionEntity,
          reportes: informacionEntity?.reportes?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="transotasApp.informacion.home.createOrEditLabel" data-cy="InformacionCreateUpdateHeading">
            <Translate contentKey="transotasApp.informacion.home.createOrEditLabel">Create or edit a Informacion</Translate>
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
                  id="informacion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('transotasApp.informacion.comentarios')}
                id="informacion-comentarios"
                name="comentarios"
                data-cy="comentarios"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.vistas')}
                id="informacion-vistas"
                name="vistas"
                data-cy="vistas"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.rating')}
                id="informacion-rating"
                name="rating"
                data-cy="rating"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra1')}
                id="informacion-extra1"
                name="extra1"
                data-cy="extra1"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra2')}
                id="informacion-extra2"
                name="extra2"
                data-cy="extra2"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra3')}
                id="informacion-extra3"
                name="extra3"
                data-cy="extra3"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra4')}
                id="informacion-extra4"
                name="extra4"
                data-cy="extra4"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra5')}
                id="informacion-extra5"
                name="extra5"
                data-cy="extra5"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra6')}
                id="informacion-extra6"
                name="extra6"
                data-cy="extra6"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra7')}
                id="informacion-extra7"
                name="extra7"
                data-cy="extra7"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra8')}
                id="informacion-extra8"
                name="extra8"
                data-cy="extra8"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra9')}
                id="informacion-extra9"
                name="extra9"
                data-cy="extra9"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.informacion.extra10')}
                id="informacion-extra10"
                name="extra10"
                data-cy="extra10"
                type="text"
              />
              <ValidatedField
                id="informacion-reportes"
                name="reportes"
                data-cy="reportes"
                label={translate('transotasApp.informacion.reportes')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/informacion" replace color="info">
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

export default InformacionUpdate;

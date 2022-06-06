import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ILabel } from 'app/shared/model/label.model';
import { getEntities as getLabels } from 'app/entities/label/label.reducer';
import { IExporter } from 'app/shared/model/exporter.model';
import { getEntities as getExporters } from 'app/entities/exporter/exporter.reducer';
import { IPhytoChina } from 'app/shared/model/phyto-china.model';
import { getEntities as getPhytoChinas } from 'app/entities/phyto-china/phyto-china.reducer';
import { IDispatchs } from 'app/shared/model/dispatchs.model';
import { getEntities as getDispatchs } from 'app/entities/dispatchs/dispatchs.reducer';
import { IPackages } from 'app/shared/model/packages.model';
import { getEntities as getPackages } from 'app/entities/packages/packages.reducer';
import { IWeight } from 'app/shared/model/weight.model';
import { getEntities as getWeights } from 'app/entities/weight/weight.reducer';
import { ISpecie } from 'app/shared/model/specie.model';
import { getEntities as getSpecies } from 'app/entities/specie/specie.reducer';
import { IVariety } from 'app/shared/model/variety.model';
import { getEntities as getVarieties } from 'app/entities/variety/variety.reducer';
import { IColor } from 'app/shared/model/color.model';
import { getEntities as getColors } from 'app/entities/color/color.reducer';
import { IFinalOverall } from 'app/shared/model/final-overall.model';
import { getEntities as getFinalOveralls } from 'app/entities/final-overall/final-overall.reducer';
import { IListSizes } from 'app/shared/model/list-sizes.model';
import { getEntities as getListSizes } from 'app/entities/list-sizes/list-sizes.reducer';
import { IInspection } from 'app/shared/model/inspection.model';
import { getEntity, updateEntity, createEntity, reset } from './inspection.reducer';

export const InspectionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const labels = useAppSelector(state => state.label.entities);
  const exporters = useAppSelector(state => state.exporter.entities);
  const phytoChinas = useAppSelector(state => state.phytoChina.entities);
  const dispatchs = useAppSelector(state => state.dispatchs.entities);
  const packages = useAppSelector(state => state.packages.entities);
  const weights = useAppSelector(state => state.weight.entities);
  const species = useAppSelector(state => state.specie.entities);
  const varieties = useAppSelector(state => state.variety.entities);
  const colors = useAppSelector(state => state.color.entities);
  const finalOveralls = useAppSelector(state => state.finalOverall.entities);
  const listSizes = useAppSelector(state => state.listSizes.entities);
  const inspectionEntity = useAppSelector(state => state.inspection.entity);
  const loading = useAppSelector(state => state.inspection.loading);
  const updating = useAppSelector(state => state.inspection.updating);
  const updateSuccess = useAppSelector(state => state.inspection.updateSuccess);
  const handleClose = () => {
    props.history.push('/inspection' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getLabels({}));
    dispatch(getExporters({}));
    dispatch(getPhytoChinas({}));
    dispatch(getDispatchs({}));
    dispatch(getPackages({}));
    dispatch(getWeights({}));
    dispatch(getSpecies({}));
    dispatch(getVarieties({}));
    dispatch(getColors({}));
    dispatch(getFinalOveralls({}));
    dispatch(getListSizes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.reportDate = convertDateTimeToServer(values.reportDate);
    values.inspectionDate = convertDateTimeToServer(values.inspectionDate);
    values.packingDate = convertDateTimeToServer(values.packingDate);

    const entity = {
      ...inspectionEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      label: labels.find(it => it.id.toString() === values.label.toString()),
      exporter: exporters.find(it => it.id.toString() === values.exporter.toString()),
      phytoChina: phytoChinas.find(it => it.id.toString() === values.phytoChina.toString()),
      dispatchs: dispatchs.find(it => it.id.toString() === values.dispatchs.toString()),
      packages: packages.find(it => it.id.toString() === values.packages.toString()),
      weight: weights.find(it => it.id.toString() === values.weight.toString()),
      specie: species.find(it => it.id.toString() === values.specie.toString()),
      variety: varieties.find(it => it.id.toString() === values.variety.toString()),
      color: colors.find(it => it.id.toString() === values.color.toString()),
      finalOverall: finalOveralls.find(it => it.id.toString() === values.finalOverall.toString()),
      listSizes: listSizes.find(it => it.id.toString() === values.listSizes.toString()),
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
          reportDate: displayDefaultDateTime(),
          inspectionDate: displayDefaultDateTime(),
          packingDate: displayDefaultDateTime(),
        }
      : {
          ...inspectionEntity,
          reportDate: convertDateTimeFromServer(inspectionEntity.reportDate),
          inspectionDate: convertDateTimeFromServer(inspectionEntity.inspectionDate),
          packingDate: convertDateTimeFromServer(inspectionEntity.packingDate),
          user: inspectionEntity?.user?.id,
          label: inspectionEntity?.label?.id,
          exporter: inspectionEntity?.exporter?.id,
          phytoChina: inspectionEntity?.phytoChina?.id,
          dispatchs: inspectionEntity?.dispatchs?.id,
          packages: inspectionEntity?.packages?.id,
          weight: inspectionEntity?.weight?.id,
          specie: inspectionEntity?.specie?.id,
          variety: inspectionEntity?.variety?.id,
          color: inspectionEntity?.color?.id,
          finalOverall: inspectionEntity?.finalOverall?.id,
          listSizes: inspectionEntity?.listSizes?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reportappApp.inspection.home.createOrEditLabel" data-cy="InspectionCreateUpdateHeading">
            <Translate contentKey="reportappApp.inspection.home.createOrEditLabel">Create or edit a Inspection</Translate>
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
                  id="inspection-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('reportappApp.inspection.reportDate')}
                id="inspection-reportDate"
                name="reportDate"
                data-cy="reportDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('reportappApp.inspection.description')}
                id="inspection-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('reportappApp.inspection.place')}
                id="inspection-place"
                name="place"
                data-cy="place"
                type="text"
              />
              <ValidatedField
                label={translate('reportappApp.inspection.inspectionDate')}
                id="inspection-inspectionDate"
                name="inspectionDate"
                data-cy="inspectionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('reportappApp.inspection.grower')}
                id="inspection-grower"
                name="grower"
                data-cy="grower"
                type="text"
              />
              <ValidatedField
                label={translate('reportappApp.inspection.packingDate')}
                id="inspection-packingDate"
                name="packingDate"
                data-cy="packingDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('reportappApp.inspection.observations')}
                id="inspection-observations"
                name="observations"
                data-cy="observations"
                type="text"
              />
              <ValidatedField
                label={translate('reportappApp.inspection.finalRecomendations')}
                id="inspection-finalRecomendations"
                name="finalRecomendations"
                data-cy="finalRecomendations"
                type="text"
              />
              <ValidatedField
                label={translate('reportappApp.inspection.extraDetails')}
                id="inspection-extraDetails"
                name="extraDetails"
                data-cy="extraDetails"
                type="text"
              />
              <ValidatedField
                id="inspection-user"
                name="user"
                data-cy="user"
                label={translate('reportappApp.inspection.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-label"
                name="label"
                data-cy="label"
                label={translate('reportappApp.inspection.label')}
                type="select"
              >
                <option value="" key="0" />
                {labels
                  ? labels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-exporter"
                name="exporter"
                data-cy="exporter"
                label={translate('reportappApp.inspection.exporter')}
                type="select"
              >
                <option value="" key="0" />
                {exporters
                  ? exporters.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-phytoChina"
                name="phytoChina"
                data-cy="phytoChina"
                label={translate('reportappApp.inspection.phytoChina')}
                type="select"
              >
                <option value="" key="0" />
                {phytoChinas
                  ? phytoChinas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-dispatchs"
                name="dispatchs"
                data-cy="dispatchs"
                label={translate('reportappApp.inspection.dispatchs')}
                type="select"
              >
                <option value="" key="0" />
                {dispatchs
                  ? dispatchs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-packages"
                name="packages"
                data-cy="packages"
                label={translate('reportappApp.inspection.packages')}
                type="select"
              >
                <option value="" key="0" />
                {packages
                  ? packages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-weight"
                name="weight"
                data-cy="weight"
                label={translate('reportappApp.inspection.weight')}
                type="select"
              >
                <option value="" key="0" />
                {weights
                  ? weights.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-specie"
                name="specie"
                data-cy="specie"
                label={translate('reportappApp.inspection.specie')}
                type="select"
              >
                <option value="" key="0" />
                {species
                  ? species.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-variety"
                name="variety"
                data-cy="variety"
                label={translate('reportappApp.inspection.variety')}
                type="select"
              >
                <option value="" key="0" />
                {varieties
                  ? varieties.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-color"
                name="color"
                data-cy="color"
                label={translate('reportappApp.inspection.color')}
                type="select"
              >
                <option value="" key="0" />
                {colors
                  ? colors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-finalOverall"
                name="finalOverall"
                data-cy="finalOverall"
                label={translate('reportappApp.inspection.finalOverall')}
                type="select"
              >
                <option value="" key="0" />
                {finalOveralls
                  ? finalOveralls.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inspection-listSizes"
                name="listSizes"
                data-cy="listSizes"
                label={translate('reportappApp.inspection.listSizes')}
                type="select"
              >
                <option value="" key="0" />
                {listSizes
                  ? listSizes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.description}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/inspection" replace color="info">
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

export default InspectionUpdate;

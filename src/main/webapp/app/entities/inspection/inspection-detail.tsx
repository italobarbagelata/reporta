import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './inspection.reducer';

export const InspectionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const inspectionEntity = useAppSelector(state => state.inspection.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inspectionDetailsHeading">
          <Translate contentKey="reportappApp.inspection.detail.title">Inspection</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{inspectionEntity.id}</dd>
          <dt>
            <span id="reportDate">
              <Translate contentKey="reportappApp.inspection.reportDate">Report Date</Translate>
            </span>
          </dt>
          <dd>
            {inspectionEntity.reportDate ? <TextFormat value={inspectionEntity.reportDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="reportappApp.inspection.description">Description</Translate>
            </span>
          </dt>
          <dd>{inspectionEntity.description}</dd>
          <dt>
            <span id="place">
              <Translate contentKey="reportappApp.inspection.place">Place</Translate>
            </span>
          </dt>
          <dd>{inspectionEntity.place}</dd>
          <dt>
            <span id="inspectionDate">
              <Translate contentKey="reportappApp.inspection.inspectionDate">Inspection Date</Translate>
            </span>
          </dt>
          <dd>
            {inspectionEntity.inspectionDate ? (
              <TextFormat value={inspectionEntity.inspectionDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="grower">
              <Translate contentKey="reportappApp.inspection.grower">Grower</Translate>
            </span>
          </dt>
          <dd>{inspectionEntity.grower}</dd>
          <dt>
            <span id="packingDate">
              <Translate contentKey="reportappApp.inspection.packingDate">Packing Date</Translate>
            </span>
          </dt>
          <dd>
            {inspectionEntity.packingDate ? <TextFormat value={inspectionEntity.packingDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="observations">
              <Translate contentKey="reportappApp.inspection.observations">Observations</Translate>
            </span>
          </dt>
          <dd>{inspectionEntity.observations}</dd>
          <dt>
            <span id="finalRecomendations">
              <Translate contentKey="reportappApp.inspection.finalRecomendations">Final Recomendations</Translate>
            </span>
          </dt>
          <dd>{inspectionEntity.finalRecomendations}</dd>
          <dt>
            <span id="extraDetails">
              <Translate contentKey="reportappApp.inspection.extraDetails">Extra Details</Translate>
            </span>
          </dt>
          <dd>{inspectionEntity.extraDetails}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.user">User</Translate>
          </dt>
          <dd>{inspectionEntity.user ? inspectionEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.label">Label</Translate>
          </dt>
          <dd>{inspectionEntity.label ? inspectionEntity.label.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.exporter">Exporter</Translate>
          </dt>
          <dd>{inspectionEntity.exporter ? inspectionEntity.exporter.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.phytoChina">Phyto China</Translate>
          </dt>
          <dd>{inspectionEntity.phytoChina ? inspectionEntity.phytoChina.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.dispatchs">Dispatchs</Translate>
          </dt>
          <dd>{inspectionEntity.dispatchs ? inspectionEntity.dispatchs.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.packages">Packages</Translate>
          </dt>
          <dd>{inspectionEntity.packages ? inspectionEntity.packages.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.weight">Weight</Translate>
          </dt>
          <dd>{inspectionEntity.weight ? inspectionEntity.weight.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.specie">Specie</Translate>
          </dt>
          <dd>{inspectionEntity.specie ? inspectionEntity.specie.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.variety">Variety</Translate>
          </dt>
          <dd>{inspectionEntity.variety ? inspectionEntity.variety.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.color">Color</Translate>
          </dt>
          <dd>{inspectionEntity.color ? inspectionEntity.color.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.finalOverall">Final Overall</Translate>
          </dt>
          <dd>{inspectionEntity.finalOverall ? inspectionEntity.finalOverall.description : ''}</dd>
          <dt>
            <Translate contentKey="reportappApp.inspection.listSizes">List Sizes</Translate>
          </dt>
          <dd>{inspectionEntity.listSizes ? inspectionEntity.listSizes.description : ''}</dd>
        </dl>
        <Button tag={Link} to="/inspection" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/inspection/${inspectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InspectionDetail;

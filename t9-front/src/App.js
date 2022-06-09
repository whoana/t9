import React from "react";
import styled from "styled-components";
import "@progress/kendo-theme-default/dist/all.css";
import { Button } from "@progress/kendo-react-buttons";
import { Grid, GridColumn } from "@progress/kendo-react-grid";
import { DateInput } from "@progress/kendo-react-dateinputs";
import { Loader } from "@progress/kendo-react-indicators";
import { orderBy } from "@progress/kendo-data-query";
import { Label } from "@progress/kendo-react-labels";
import { Input } from "@progress/kendo-react-inputs";
import axios from "axios";
import * as moment from "moment";

const ROW_COUNT = 40;
const initialDataState = {
  skip: 0,
  take: ROW_COUNT,
};

const initialSort = [
  {
    field: "trackingDate",
    dir: "desc",
  },
];

const now1 = moment();
const now2 = moment().add(-3, "h");

//Layout components
const Container = styled.div`
  display: flex;
  flex-direction: column;
  padding: 40px;
  margin: 0;
`;

const TitleContainer = styled.div`
  padding: 10px 0px 0px 10px;
  background-color: #845ef7;
  color: #ff6b6b;
  //border-radius: 20px;
`;

const FilterContainer = styled.div`
  display: flex;
  flex-direction: row;
  padding: 10px;
  background-color: #845ef7;
  color: #ff6b6b;
  //border-radius: 20px;
`;

const BodyContainer = styled.div`
  padding: 10px;
  background-color: #e8eaf6;
`;

const FilterItems = styled.div`
  display: flex;
  flex-direction: row;
  flex: 1;
`;

const ButtonItems = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  flex: 1;
`;

function App() {
  const [logs, setLogs] = React.useState([]);

  const [logsLength, setLogsLength] = React.useState(0);

  const [fromDate, setFromDate] = React.useState(now2.toDate());
  const [toDate, setToDate] = React.useState(now1.toDate());
  const [integrationId, setIntegrationId] = React.useState(null);

  const [searchable, setSearchable] = React.useState(true);

  const [sort, setSort] = React.useState(initialSort);

  const changeFromDate = (event) => {
    setFromDate(event.value);
  };

  const changeToDate = (event) => {
    setToDate(event.value);
  };

  const changeIntegrationId = (event) => {
    setIntegrationId(event.value);
  };

  const handleSearch = (firstSearch, offset) => {
    if (firstSearch) {
      setLogs([]);
      setLogsLength(0);
    }
    setSearchable(false);
    let fromDateString = moment(fromDate).format("YYYYMMDDHHmmss").toString();
    let toDateString = moment(toDate).format("YYYYMMDDHHmmss").toString();
    let startTime = moment().format("YYYYMMDDHHmmssSSS").toString();
    //let url = "http://10.10.1.10:8090/trace/services/tlogs?method=GET";
    let url =
      "http://" +
      window.location.hostname +
      ":" +
      window.location.port +
      "/trace/services/tlogs?method=GET";
    const options = {
      url: url,
      method: "POST",
      header: {
        Accept: "application/json",
        "Content-Type": "application/json; charset=utf-8",
      },
      data: {
        objectType: "ComMessage",
        requestObject: {
          fromDate: fromDateString,
          toDate: toDateString,
          offset: offset,
          rowCount: ROW_COUNT,
          integrationId: integrationId,
          firstSearch: firstSearch,
        },
        startTime: startTime,
        endTime: null,
        errorCd: "0",
        errorMsg: "",
        userId: "iip",
        appId: "t9-front",
        checkSession: false,
      },
    };

    axios(options)
      .then((response) => {
        console.log(response);
        setLogs(response.data.responseObject.list);
        if (firstSearch) setLogsLength(response.data.responseObject.totalCount);
      })
      .catch((e) => {
        console.log(e);
      })
      .finally(() => {
        setSearchable(true);
      });
  };

  const [page, setPage] = React.useState(initialDataState);

  const pageChange = (event) => {
    handleSearch(false, event.page.skip);
    setPage(event.page);
  };

  return (
    <>
      <Container>
        <TitleContainer>
          <h2>Search TLog</h2>
        </TitleContainer>
        <FilterContainer>
          <FilterItems>
            <div className="col-12 col-md-6 example-col">
              <Label editorId={"formDateInput"}>First Date:&nbsp;</Label>
              <DateInput
                id={"formDateInput"}
                format="yyyy-MM-dd HH:mm:ss"
                value={fromDate}
                onChange={changeFromDate}
                width={"180px"}
              />
            </div>
          </FilterItems>
          <FilterItems>
            <div className="col-12 col-md-6 example-col">
              <Label editorId={"toDateInput"}>To Date:&nbsp;</Label>
              <DateInput
                id={"toDateInput"}
                format="yyyy-MM-dd HH:mm:ss"
                value={toDate}
                onChange={changeToDate}
                width={"180px"}
              />
            </div>
          </FilterItems>
          <FilterItems>
            <div className="col-12 col-md-6 example-col">
              <Label editorId={"integrationId"}>InterfaceId:&nbsp;</Label>
              <Input
                id={"integrationId"}
                value={integrationId}
                onChange={changeIntegrationId}
                style={{ width: "180px" }}
              />
            </div>
          </FilterItems>
          <FilterItems>
            <div className="col-12 col-md-6 example-col">
              {searchable ? (
                <Button
                  themeColor={"primary"}
                  disabled={!searchable}
                  onClick={() => {
                    handleSearch(true, 0);
                  }}
                >
                  Search
                </Button>
              ) : (
                <div className="col-4">
                  <Loader type={"converging-spinner"} size={"medium"} />
                </div>
              )}
            </div>
          </FilterItems>
        </FilterContainer>
        <BodyContainer>
          <Grid
            style={{
              height: "600px",
              width: "100%",
            }}
            data={orderBy(logs, sort)}
            reorderable={true}
            skip={page.skip}
            take={page.take}
            total={logsLength}
            pageable={true}
            onPageChange={pageChange}
            sortable={true}
            sort={sort}
            onSortChange={(e) => {
              setSort(e.sort);
            }}
            resizable={true}
          >
            <GridColumn field="integrationId" />
            <GridColumn field="trackingDate" />
            <GridColumn field="orgHostId" />
            <GridColumn field="interfaceId" />
            <GridColumn field="interfaceNm" />
            <GridColumn field="status" />
            <GridColumn field="businessNm" />
            <GridColumn field="channelNm" />
            <GridColumn field="dataPrDirNm" />
            <GridColumn field="appPrMethodNm" />
            <GridColumn field="sndSystemNm" />
            <GridColumn field="sndResNm" />
            <GridColumn field="rcvSystemNm" />
            <GridColumn field="rcvResNm" />
          </Grid>
        </BodyContainer>
      </Container>
    </>
  );
}

export default App;

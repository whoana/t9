import React from "react";
import styled from "styled-components";
import "@progress/kendo-theme-default/dist/all.css";
import { Button } from "@progress/kendo-react-buttons";
import { Grid, GridColumn } from "@progress/kendo-react-grid";
import products from "./products.json";
import axios from "axios";
const initialDataState = {
  skip: 0,
  take: 10,
};

const Title = styled.div`
  margin: 20px;
  padding: 10px;
`;
function App() {
  const [logs, setLogs] = React.useState([]);

  const [logsLength, setLogsLength] = React.useState(0);

  const handleSearch = () => {
    console.log("I'm button");

    const options = {
      url: "http://127.0.0.1:8090/trace/services/tlogs?method=GET",
      method: "POST",
      header: {
        Accept: "application/json",
        "Content-Type": "application/json; charset=utf-8",
      },
      data: {
        objectType: "ComMessage",
        requestObject: {
          fromDate: "20220513000000",
          toDate: "20220513235959",
          offset: 20,
          rowCount: 10,
          firstSearch: false,
        },
        startTime: "20190416105001001",
        endTime: null,
        errorCd: "0",
        errorMsg: "",
        userId: "iip",
        appId: "SU0810R01",
        checkSession: false,
      },
    };

    console.log(options);

    axios(options)
      .then((response) => {
        console.log(response);
        setLogs(response.data.responseObject.list);
        setLogsLength(response.data.responseObject.totalCount);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const [page, setPage] = React.useState(initialDataState);

  const pageChange = (event) => {
    console.log(event);
    setPage(event.page);
  };

  return (
    <div>
      <Title>
        <h1>Search Tracking</h1>
        <Button themeColor={"primary"} onClick={handleSearch}>
          Search
        </Button>
      </Title>
      <div>
        <Grid
          style={{
            height: "400px",
          }}
          data={logs}
          skip={page.skip}
          take={page.take}
          total={logsLength}
          pageable={true}
          onPageChange={pageChange}
        >
          <GridColumn field="integrationId" />
          <GridColumn field="trackingDate" />
          <GridColumn field="orgHostId" />
          <GridColumn field="interfaceId" />
          <GridColumn field="status" />
          <GridColumn field="match" />
          <GridColumn field="recordCnt" />
          <GridColumn field="dataAmt" />
          <GridColumn field="cmp" />
          <GridColumn field="cst" />
          <GridColumn field="tdc" />
          <GridColumn field="fnc" />
          <GridColumn field="erc" />
          <GridColumn field="errorCd" />
          <GridColumn field="errorMsg" />
          <GridColumn field="businessId" />
          <GridColumn field="businessNm" />
          <GridColumn field="interfaceNm" />
          <GridColumn field="channelId" />
          <GridColumn field="channelNm" />
          <GridColumn field="dataPrDir" />
          <GridColumn field="dataPrDirNm" />
          <GridColumn field="dataPrMethod" />
          <GridColumn field="dataPrMethodNm" />
          <GridColumn field="appPrMethod" />
          <GridColumn field="appPrMethodNm" />
          <GridColumn field="sndOrgId" />
          <GridColumn field="sndOrgNm" />
          <GridColumn field="sndSystemId" />
          <GridColumn field="sndSystemNm" />
          <GridColumn field="sndResType" />
          <GridColumn field="sndResNm" />
          <GridColumn field="rcvOrgId" />
          <GridColumn field="rcvOrgNm" />
          <GridColumn field="rcvSystemId" />
          <GridColumn field="rcvSystemNm" />
          <GridColumn field="rcvResType" />
          <GridColumn field="rcvResNm" />
          <GridColumn field="regDate" />
          <GridColumn field="modDate" />
          <GridColumn field="businessCd" />
          <GridColumn field="channelCd" />
          <GridColumn field="sndOrgCd" />
          <GridColumn field="rcvOrgCd" />
          <GridColumn field="sndSystemCd" />
          <GridColumn field="rcvSystemCd" />
        </Grid>
      </div>
    </div>
  );
}

export default App;

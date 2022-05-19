import React from "react";
import styled from "styled-components";
import "@progress/kendo-theme-default/dist/all.css";
import { Button } from "@progress/kendo-react-buttons";
import { Grid, GridColumn } from "@progress/kendo-react-grid";
import { DateInput } from "@progress/kendo-react-dateinputs";
import { Loader } from "@progress/kendo-react-indicators";
import { orderBy } from "@progress/kendo-data-query";
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
const now2 = now1.add(-3, "h");

const Title = styled.div`
  margin: 10px;
  padding: 10px;
  background-color: #845ef7;
  color: #ff6b6b;
  border-radius: 20px;
`;

const TitleItem = styled.div`
  margin: 10px;
  padding: 10px;
  color: #ffe3e3;
`;

const Contents = styled.div`
  margin: 10px;
  padding: 10px;
`;

function App() {
  const [logs, setLogs] = React.useState([]);

  const [logsLength, setLogsLength] = React.useState(0);

  const [fromDate, setFromDate] = React.useState(now2.toDate());
  const [toDate, setToDate] = React.useState(now1.toDate());

  const [searchable, setSearchable] = React.useState(true);

  const [sort, setSort] = React.useState(initialSort);

  const changeFromDate = (event) => {
    setFromDate(event.value);
  };

  const changeToDate = (event) => {
    setToDate(event.value);
  };

  const handleSearch = (firstSearch, offset) => {
    if (firstSearch) {
      setLogs([]);
      setLogsLength(0);
    }
    setSearchable(false);
    //url: "http://127.0.0.1:8090/trace/services/tlogs?method=GET",
    const options = {
      url:
        "http://" +
        window.location.hostname +
        ":" +
        window.location.port +
        "/trace/services/tlogs?method=GET",
      method: "POST",
      header: {
        Accept: "application/json",
        "Content-Type": "application/json; charset=utf-8",
      },
      data: {
        objectType: "ComMessage",
        requestObject: {
          fromDate: moment(fromDate).format("YYYYMMDDhhmmss").toString(), //"20220513000000",
          toDate: moment(toDate).format("YYYYMMDDhhmmss").toString(), //"20220513235959",
          offset: offset,
          rowCount: ROW_COUNT,
          firstSearch: firstSearch,
        },
        startTime: moment().format("YYYYMMDDhhmmssSSS").toString(),
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
    <div>
      <Title>
        <h1>Search TLog</h1>
        <TitleItem>
          From date:
          <DateInput
            format="yyyy-MM-dd HH:mm:ss"
            value={fromDate}
            onChange={changeFromDate}
          />
        </TitleItem>

        <TitleItem>
          To date:
          <DateInput
            format="yyyy-MM-dd HH:mm:ss"
            value={toDate}
            onChange={changeToDate}
          />
        </TitleItem>

        <TitleItem>
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
        </TitleItem>
      </Title>
      <Contents>
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
      </Contents>
    </div>
  );
}

export default App;
